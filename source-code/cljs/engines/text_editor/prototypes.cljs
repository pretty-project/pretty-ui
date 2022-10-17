
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.text-editor.prototypes
    (:require [mid-fruits.candy            :refer [param]]
              [engines.text-editor.helpers :as helpers]
              [re-frame.api                :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;
  ; @return (map)
  ;  {:placeholder (metamorphic-content)
  ;   :value-path (vector)}
  [editor-id editor-props]
  (merge {:placeholder :write-something!
          :value-path  (helpers/default-value-path editor-id)}
         (param editor-props)))

(defn ckeditor5-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;
  ; @return (map)
  ;  {}
  [editor-id editor-props]
  (merge {:buttons [:bold :italic :underline :fontColor]}
         editor-props
         {:on-blur   #(r/dispatch-sync [:environment/quit-type-mode!])
          :on-focus  #(r/dispatch-sync [:environment/set-type-mode!])
          :on-change helpers/on-change-f
          :value     (helpers/get-editor-input editor-id)
          :font-colors [{:color "var( --color )"           :label @(r/subscribe [:dictionary/look-up :default-color])}
                        {:color "var( --color-muted )"     :label @(r/subscribe [:dictionary/look-up :muted-color])}
                        {:color "var( --color-highlight )" :label @(r/subscribe [:dictionary/look-up :highlight-color])}
                        {:color "var( --white-xx-Light )"  :label @(r/subscribe [:dictionary/look-up :white]) :hasBorder true}]
          :background-colors [{:color "hsl(0, 75%, 60%)"  :label "Red"}
                              {:color "hsl(30, 75%, 60%)" :label "Orange"}
                              {:color "hsl(60, 75%, 60%)" :label "Yellow"}
                              {:color "hsl(90, 75%, 60%"  :label "Light green"}]}))
