
(ns jodit.helpers
    (:require [clojure.string]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-whitespaces
  ; @ignore
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
  ; @ignore
  ;
  ; @param (keywords in vector) buttons
  ; [:bold, :italic, :underline, :font, :font-size, :cut, :copy, :paste
  ;  :link, :undo, :redo, :brush]
  ;
  ; @example
  ; (parse-buttons [:bold :italic])
  ; =>
  ; "bold, italic"
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
  ; @ignore
  ;
  ; @param (map) editor-props
  ; {:autofocus? (boolean)(opt)
  ;  :buttons (keywords in vector)
  ;  :disabled? (boolean)(opt)
  ;  :min-height (px)
  ;  :placeholder (metamorphic-content)}
  ;
  ; @return (map)
  ; {:autofocus (boolean)
  ;  :buttons (string)
  ;  :cleanHTML (boolean)
  ;  :cleanWhitespace (boolean)
  ;  :disabled (boolean)
  ;  :insert-as (keyword)
  ;  :language (string)
  ;  :minHeight (string)
  ;  :placeholder (string)
  ;  :showCharsCounter (boolean)
  ;  :showWordsCounter (boolean)
  ;  :showXPathInStatusbar (boolean)}
  [{:keys [autofocus? buttons disabled? insert-as min-height placeholder]}]
  (let [placeholder       @(r/subscribe [:x.dictionary/look-up placeholder])
        selected-language @(r/subscribe [:x.locales/get-selected-language])]
       {:autofocus              autofocus?
        :buttons                (parse-buttons buttons)
        :buttonsXS              (parse-buttons buttons)
        :buttonsSM              (parse-buttons buttons)
        :buttonsMD              (parse-buttons buttons)
        :cleanHTML              true
        :cleanWhitespace        true

        ; A {:disabled? true} állapotba léptetett jodit hülyén néz ki (nagyon szürke!),
        ; ezért, amíg kifejezetten nem szükséges, addig ez a funkció ki lesz kapcsolva!
        ; :disabled disabled?
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
