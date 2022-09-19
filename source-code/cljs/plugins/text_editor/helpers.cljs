
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
              [plugins.text-editor.config :as config]
              [plugins.text-editor.state  :as state]
              [x.app-core.api             :as a]

              ; TEMP
              [react]))



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
  ; @param (string) n
  ;
  ; @return (string)
  [n]
  ; TODO
  ; @Paul – Mi a feladata a remove-whitespaces függvénynek?
  (-> n (clojure.string/replace #"\n" "")
        (clojure.string/replace #"\t" "")))

(defn parse-buttons
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
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [editor-id]
  (get @state/EDITOR-CONTENTS editor-id))

(defn get-editor-change
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [editor-id]
  (get @state/EDITOR-CHANGES editor-id))

(defn set-editor-content!
  ; @param (keyword) editor-id
  ; @param (string) editor-content
  ;
  ; @return (string)
  [editor-id editor-content]
  (swap! state/EDITOR-CONTENTS assoc editor-id editor-content))

(defn update-editor-change!
  ; @param (keyword) editor-id
  ; @param (string) editor-change
  ;
  ; @return (string)
  [editor-id editor-change]
  (swap! state/EDITOR-CHANGES assoc editor-id {:change editor-change
                                               :changed-at (random-uuid)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-change-f
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:value-path (vector)}
  ; @param (string) editor-content
  [editor-id {:keys [value-path]} editor-content]
  ; Az on-change-f függvény
  (let [editor-content (remove-whitespaces editor-content)]
       (set-editor-content! editor-id editor-content)
       (a/dispatch-last config/TYPE-ENDED-AFTER [:db/set-item! value-path editor-content])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------



(defn memofn
  ([f]
   (react/useMemo f #js []))
  ([f deps]
   (react/useMemo f (to-array deps))))

(defn jodit-config
  ; @param (map) editor-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [{:keys [autofocus? buttons disabled? min-height placeholder]}]
  (let [placeholder       @(a/subscribe [:dictionary/look-up placeholder])
        selected-language @(a/subscribe [:locales/get-selected-language])]
       {:autofocus            autofocus?
        :buttons              (parse-buttons buttons)
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
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;
  ; @return (map)
  ;  {}
  [editor-id editor-props]
  (let [;editor-change (get-editor-change editor-id)]
        editor-changed-at (get-in @state/EDITOR-CHANGES [editor-id :changed-at])]
       (memofn (fn [] {:config    (jodit-config          editor-props)
                       :onChange #(on-change-f editor-id editor-props %)
                       ;:key       (get-in @state/EDITOR-CHANGES [editor-id :changed-at])
                       ;:onFocus  #(a/dispatch [:environment/set-type-mode!])
                       ;:onBlur   #(a/dispatch [:environment/quit-type-mode!])
                       :tabIndex  1
                       ;:value    (get-in @state/EDITOR-CHANGES [editor-id :change])}))
                       :value     "static-string"})
               [editor-changed-at])))
