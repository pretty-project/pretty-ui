
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
  ; @param (string) n
  ;
  ; @return (string)
  [n]
  (-> n (clojure.string/replace #"\n" "")
        (clojure.string/replace #"\t" "")))

(defn parse-buttons
  ; @param (keywords in vector) buttons
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
  (swap! state/EDITOR-CHANGES assoc editor-id editor-change))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-change-f
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:value-path (vector)}
  ; @param (string) editor-content
  [editor-id {:keys [value-path]} editor-content]
  (let [editor-content (remove-whitespaces editor-content)]
       (set-editor-content! editor-id editor-content)
       (a/dispatch-last config/TYPE-ENDED-AFTER [:db/set-item! value-path editor-content])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (let [editor-change (get-editor-change editor-id)
        stored-value @(a/subscribe [:db/get-item (:value-path editor-props)])]
       {:config    (jodit-config          editor-props)
        :onChange #(on-change-f editor-id editor-props %)
        :tabIndex  1
        :value     (get @state/EDITOR-CHANGES editor-id)}))
