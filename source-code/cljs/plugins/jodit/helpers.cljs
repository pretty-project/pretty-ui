
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.jodit.helpers
    (:require [clojure.string]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-whitespaces
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) n
  ;
  ; @return (string)
  [n]
  ; TODO
  ; @Paul – Mi a feladata a remove-whitespaces függvénynek?
  (-> n (clojure.string/replace #"\n" "")
        (clojure.string/replace #"\t" "")))

(defn parse-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keywords in vector) buttons
  ;  [:bold, :italic, :underline, :font, :font-size, :cut, :copy, :paste
  ;   :link, :undo, :redo, :brush]
  ;
  ; @example
  ;  (parse-buttons [:bold :italic])
  ;  =>
  ;  "bold, italic"
  ;
  ; @result (string)
  [buttons]
  (letfn [(f [result dex button] (if (= dex 0)
                                     (str             (name button))
                                     (str result ", " (name button))))]
         (reduce-kv f nil buttons)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn jodit-config
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) editor-props
  ;  {:autofocus? (boolean)(opt)
  ;   :buttons (keywords in vector)
  ;   :disabled? (boolean)(opt)
  ;   :min-height (px)
  ;   :placeholder (metamorphic-content)}
  ;
  ; @return (map)
  ;  {:autofocus (boolean)
  ;   :buttons (string)
  ;   :cleanHTML (boolean)
  ;   :cleanWhitespace (boolean)
  ;   :disabled (boolean)
  ;   :insert-as (keyword)
  ;   :language (string)
  ;   :minHeight (string)
  ;   :placeholder (string)
  ;   :showCharsCounter (boolean)
  ;   :showWordsCounter (boolean)
  ;   :showXPathInStatusbar (boolean)}
  [{:keys [autofocus? buttons disabled? insert-as min-height placeholder]}]
  (let [placeholder       @(r/subscribe [:dictionary/look-up placeholder])
        selected-language @(r/subscribe [:locales/get-selected-language])]
       {:autofocus              autofocus?
        :buttons                (parse-buttons buttons)
        :buttonsXS              (parse-buttons buttons)
        :buttonsSM              (parse-buttons buttons)
        :buttonsMD              (parse-buttons buttons)
        :cleanHTML              true
        :cleanWhitespace        true

       ; A {:disabled? true} állapotba léptetett jodit hülyén néz ki (nagyon szürke!),
       ; ezért, amíg kifejezetten nem szükséges, addig ez a funkció ki lesz kapcsolva!
       ;:disabled               disabled?
        :language               (name selected-language)
        :minHeight              (str min-height)
        :placeholder            placeholder
        :showCharsCounter       false
        :showWordsCounter       false
        :showXPathInStatusbar   false

        ; A külső helyről származó beillesztett szöveg formázásának megtartására
        ; a legtöbb esetben nincs szükség, sőt egy weboldal megjelenését negatívan
        ; befolyásolja, ha a szerkesztő egy szöveg beillesztésekor megtartaja
        ; a külső forrás formázását (pl. betűtípus, színek, ...).
        ;
        ; Előfordulhat, hogy a tisztított html (cleared html) formátumban beillesztett
        ; szöveg a nem tisztított html (as html) formátumhoz képest bizonyos esetekben
        ; egyes sortöréseket is elveszíthet! Ez a forrás-szöveg nem megfelelő
        ; formázásból adódhat!
        :defaultActionOnPaste   "insert_clear_html"
        :askBeforePasteFromWord false
        :askBeforePasteHTML     false}))

(defn jodit-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {}
  ;
  ; @return (map)
  ;  {:config (map)
  ;   :on-blur (function)
  ;   :on-change (function)
  ;   :on-focus (function)
  ;   :key (string)
  ;   :tabIndex (integer)
  ;   :value (string)}
  [editor-id {:keys [on-blur on-change on-focus update-trigger value] :as editor-props}]
  ; A key paraméter megváltozásának hatására a szerkesztő tartalma a value paraméter aktuális
  ; értéke lesz.
  ; Pl.: Ha a value paraméter a szerkesztő React-fába csatolásakor megkapja a szerkesztő kezdeti
  ;      tartalmát és később a felhasználó megnyomja a "Visszaállítás" gombot, hogy a szerkesztő
  ;      tartalma újra a megnyitáskori állapot szerinti legyen, akkor szükséges megváltoztatni
  ;      a key paraméter értékét, mivel a visszaállítás után a value paraméter "új" értéke
  ;      megegyezik az utoljára a megnyitáskor átadott értékével ezért a szerkesztő
  ;      component-did-update életciklusa nem triggerelődne.
  ;
  ; A key paraméter esetleges változtatása a szerkesztő villanását okozza.
  ;
  ; TODO
  ; Az on-change függvény számára a mező értékét is szükséges átadni!
  {:config     (jodit-config editor-props)
   :on-blur    (fn [_] (if on-blur   (on-blur   editor-id editor-props)))
   :on-focus   (fn [_] (if on-focus  (on-focus  editor-id editor-props)))
   :on-change  (fn [_] (if on-change (on-change editor-id editor-props)))
   :key        update-trigger
   :value      value
   :tabIndex   1})
