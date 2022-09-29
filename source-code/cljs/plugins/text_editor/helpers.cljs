
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.text-editor.helpers
    (:require [clojure.string]
              [mid-fruits.random          :as random]
              [plugins.text-editor.config :as config]
              [plugins.text-editor.state  :as state]
              [x.app-core.api             :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-value-path
  ; @param (keyword) editor-id
  ;
  ; @return (vector)
  [editor-id]
  [:plugins :text-editor/editor-content editor-id])



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
  ;
  ; @example
  ;  (helpers/parse-buttons [:bold :italic])
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

(defn get-editor-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [editor-id]
  ; XXX#9910
  ; A text-editor a state/EDITOR-CONTENTS atomban is eltárolja az aktuális értékét,
  ; amit arra használ, hogy ha a value-path Re-Frame adatbázis útvonalon tárolt
  ; érték megváltozik, akkor a [:text-editor/hack-9910 ...] esemény összehasonlítja
  ; az adatbázisban tárolt értéket az atomban tárolt értékkel és megállapítja,
  ; hogy az adatbázisban tárolt érték megegyezik-e a text-editorba írt szöveggel.
  (get @state/EDITOR-CONTENTS editor-id))

(defn get-editor-difference
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [editor-id]
  ; XXX#9910
  ; Ha a value-path Re-Frame adatbázis útvonalon tárolt érték megváltozásakor
  ; a [:text-editor/hack-9910 ...] esemény azt állapítja meg, hogy az adatbázisban
  ; tárolt érték és a text-editor aktuális értéke nem egyezik meg, akkor úgy
  ; értelmezi, hogy az adatbázisban tárolt érték külső hatásra változott meg,
  ; ezért szükséges a text-editor tartalmát aktualizálni, amit a state/EDITOR-DIFFERENCES
  ; atomban tárolt érték megváltoztatásával ér el.
  (get-in @state/EDITOR-DIFFERENCES [editor-id :difference]))

(defn get-editor-difference-key
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [editor-id]
  ; XXX#9910
  ; A text-editor számára key paraméterként átadott difference-key kulcs
  ; megváltoztatásával a text-editor minden esetben aktualizálja az értékét
  ; a value paraméterként kapott értékkel (akkor is ha az nem változott meg).
  (get-in @state/EDITOR-DIFFERENCES [editor-id :difference-key]))

(defn set-editor-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) editor-content
  [editor-id editor-content]
  ; XXX#9910
  (swap! state/EDITOR-CONTENTS assoc editor-id editor-content))

(defn update-editor-difference!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) editor-difference
  [editor-id editor-difference]
  ; XXX#9910
  ; Ha a state/EDITOR-DIFFERENCES atomban tárolt érték megváltozik, amit
  ; a text-editor value paraméterként kap meg, akkor a text-editor tartalma
  ; aktualizálódik. Ha a text-editor értéke ugyanarra az értékre aktualizálódna,
  ; mint az utoljára a state/EDITOR-DIFFERENCES atomba eltárolt érték, akkor
  ; szükséges a difference-key kulcsot megváltoztatni, ellenkező esetben
  ; a text-editor nem aktualizálná az értékét a value paraméterként átadott
  ; értékkel (mivel az nem változik ilyen esetben).
  ; Pl.: A text-editor React-fába csatolásakor a state/EDITOR-DIFFERENCES
  ;      atom értéke a [:text-editor/hack-9910 ...] esemény hatására megkapja
  ;      a value-path Re-Frame adatbázis útvonalon tárolt értéket,
  ;      így az a text-editor kezdeti értékeként a szövegmezőbe íródik.
  ;      Ha a felhasználó átírja a text-editor tartalmát, majd rákattint egy az
  ;      eredeti kezdeti értéket visszaállító gombra, akkor a text-editor értékét
  ;      újból a state/EDITOR-DIFFERENCES atom értékével szükséges aktualizálni,
  ;      viszont mivel az az atom utoljára is a kezdeti értéket tárolta és a
  ;      visszaállítás hatására is, ezért ilyenkor szükséges a difference-key
  ;      értékét módosítani, hogy a text-editor reagáljon egy változásra.
  (if (= editor-difference (get-editor-difference editor-id))
      (swap! state/EDITOR-DIFFERENCES merge {editor-id {:difference-key (random/generate-string)
                                                        :difference editor-difference}})
      (swap! state/EDITOR-DIFFERENCES merge {editor-id {:difference editor-difference}})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-change-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:value-path (vector)}
  ; @param (string) editor-content
  [editor-id {:keys [value-path]} editor-content]
  ; Az on-change-f függvény a text-editor aktuális tartalmát ...
  ; ... a set-editor-content! függvénnyel az EDITOR-CONTENTS atomba írja.
  ; ... a dispatch-last függvénnyel a value-path Re-Frame adatbázis útvonalra írja,
  ;     ha a felhasználó már befejezte a gépelést.
  (let [editor-content (remove-whitespaces editor-content)]
       (set-editor-content! editor-id editor-content)
       (a/dispatch-last     config/TYPE-ENDED-AFTER [:db/set-item! value-path editor-content])))



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
  ;   :language (string)
  ;   :minHeight (string)
  ;   :placeholder (string)
  ;   :showCharsCounter (boolean)
  ;   :showWordsCounter (boolean)
  ;   :showXPathInStatusbar (boolean)}
  [{:keys [autofocus? buttons disabled? min-height placeholder]}]
  (let [placeholder       @(a/subscribe [:dictionary/look-up placeholder])
        selected-language @(a/subscribe [:locales/get-selected-language])]
       {:autofocus            autofocus?
        :buttons              (parse-buttons buttons)
        :buttonsXS            (parse-buttons buttons)
        :buttonsSM            (parse-buttons buttons)
        :buttonsMD            (parse-buttons buttons)
        :cleanHTML            true
        :cleanWhitespace      true
       ; A {:disabled? true} állapotba léptetett jodit hülyén néz ki (nagyon szürke!),
       ; ezért, amíg kifejezetten nem szükséges, addig ez a funkció ki lesz kapcsolva!
       ;:disabled             disabled?
        :language             (name selected-language)
        :minHeight            (str min-height)
        :placeholder          placeholder
        :showCharsCounter     false
        :showWordsCounter     false
        :showXPathInStatusbar false}))

(defn jodit-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;
  ; @return (map)
  ;  {:config (map)
  ;   :onBlur (function)
  ;   :onChange (function)
  ;   :onFocus (function)
  ;   :key (string)
  ;   :tabIndex (integer)
  ;   :value (string)}
  [editor-id editor-props]
  ; XXX#9910
  ; A key paraméter esetleges változtatása a text-editor villanását okozza.
  ; A difference-key változtatása helyett más megoldás is alkalmazható!
  {:onBlur   #(a/dispatch [:environment/quit-type-mode!])
   :onFocus  #(a/dispatch [:environment/set-type-mode!])
   :onChange #(on-change-f editor-id editor-props %)
   :config    (jodit-config              editor-props)
   :key       (get-editor-difference-key editor-id)
   :value     (get-editor-difference     editor-id)
   :tabIndex  1})
