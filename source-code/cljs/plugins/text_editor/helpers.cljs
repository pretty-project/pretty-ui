
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
  (get-in @state/EDITOR-CONTENTS editor-id))

(defn set-editor-content!
  ; @param (keyword) editor-id
  ; @param (string) editor-content
  ;
  ; @return (string)
  [editor-id editor-content]
  (swap! state/EDITOR-CONTENTS assoc editor-id editor-content))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-change-f
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:value-path (vector)}
  ; @param (string) editor-content
  [editor-id {:keys [value-path]} editor-content]
  (let [editor-content (remove-whitespaces editor-content)]
       (a/dispatch-last config/TYPE-ENDED-AFTER [:db/set-item! value-path editor-content])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn jodit-config
  ; @param (map) editor-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [{:keys [autofocus? buttons disabled? min-height]}]
  (let [placeholder       @(a/subscribe [:dictionary/look-up :write-something!])
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
  ;  {:value-path (vector)}
  ;
  ; @return (map)
  ;  {}
  [editor-id {:keys [value-path] :as editor-props}]
  (let [stored-value @(a/subscribe [:db/get-item value-path])]
       {:config    (jodit-config          editor-props)
        :onChange #(on-change-f editor-id editor-props %)
        :tabIndex  1
        :value     stored-value}))
