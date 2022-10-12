
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
              ["@ckeditor/ckeditor5-build-classic" :as ckeditor5-build-classic]
              ;["@ckeditor/ckeditor5-font"          :as ckeditor5-font]
              [mid-fruits.random                   :as random]
              [plugins.text-editor.config          :as config]
              [plugins.text-editor.state           :as state]
              [re-frame.api                        :as r]))



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
  ;  [:bold, :italic, :underline, :font, :font-size, :cut, :copy, :paste
  ;   :link, :undo, :redo, :brush]
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

(defn get-editor-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [editor-id]
  ; HACK#9910
  (get @state/EDITOR-INPUT editor-id))

(defn get-editor-output
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [editor-id]
  ; HACK#9910
  (get @state/EDITOR-OUTPUT editor-id))

(defn get-editor-trigger
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [editor-id]
  ; HACK#9910
  (get @state/EDITOR-TRIGGER editor-id))

(defn set-editor-output!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (string) editor-content
  [editor-id editor-content]
  ; HACK#9910
  (swap! state/EDITOR-OUTPUT assoc editor-id editor-content))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn synchronizer-did-update-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:value-path (vector)}
  ; @param (string) stored-value
  [editor-id editor-props stored-value]
  (when (not= stored-value (get @state/EDITOR-INPUT editor-id))
        (swap! state/EDITOR-INPUT assoc editor-id stored-value)))

(defn synchronizer-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:value-path (vector)}
  [editor-id {:keys [value-path]}]
  (let [stored-value @(r/subscribe [:db/get-item value-path])]
       (swap! state/EDITOR-INPUT assoc editor-id stored-value)))

(defn synchronizer-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  [editor-id _]
  (swap! state/EDITOR-INPUT   dissoc editor-id)
  (swap! state/EDITOR-OUTPUT  dissoc editor-id)
  (swap! state/EDITOR-TRIGGER dissoc editor-id))

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
  (set-editor-output! editor-id editor-content)
  (r/dispatch-last    config/TYPE-ENDED-AFTER [:db/set-item! value-path editor-content]))



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
  ; HACK#9910
  ; A key paraméter esetleges változtatása a text-editor villanását okozza.
  ; A difference-key változtatása helyett más megoldás is alkalmazható!
  {:config     (jodit-config          editor-props)
   :on-change #(on-change-f editor-id editor-props %)
   :on-blur   #(r/dispatch [:environment/quit-type-mode!])
   :on-focus  #(r/dispatch [:environment/set-type-mode!])
   :key        (get-editor-trigger editor-id)
   :value      (get-editor-input   editor-id)
   :tabIndex   1})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ckeditor-config
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;
  ; @return (map)
  ;  {}
  [editor-id editor-props]
  {:toolbar [:bold :italic "underline" "fontFamily"]
  ; :plugins [ckeditor5-font]

  ;:toolbar [:heading]
   :heading {:options [{:model "paragraph"           :title "Paragraph" :class "ck-heading_paragraph"}
                       {:model "heading1" :view "h1" :title "Custom #1" :class "ck-heading_heading1"}]}})

(defn ckeditor-attributes
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
  {:editor    ckeditor5-build-classic
   :config    (ckeditor-config    editor-id editor-props)
   :data      (get-editor-input   editor-id)
   :key       (get-editor-trigger editor-id)
   :on-blur  #(r/dispatch [:environment/quit-type-mode!])
   :on-focus #(r/dispatch [:environment/set-type-mode!])
   :on-change (fn [event editor] (on-change-f editor-id editor-props (-> editor .getData)))})
