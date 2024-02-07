
(ns pretty-elements.label.prototypes
    (:require [pretty-properties.api :as pretty-properties]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  [_ label-props]
  ; @bug (#9811)
  ; - The content can be an empty string before it gets its eventual value
  ;   (e.g., from a subscription or a HTTP request, etc.).
  ;   An empty placeholder and a delayed content can cause a short flickering due to inconsistent content height!
  ;   Therefore, the placeholder must have at least a blank character as its content (e.g., "\u00A0").
  (-> label-props (pretty-properties/inherit-icon-props)
                  (pretty-properties/default-border-props  {})
                  (pretty-properties/default-content-props {:placeholder "\u00A0"})
                  (pretty-properties/default-font-props    {:font-size :s :font-weight :medium})
                  (pretty-properties/default-flex-props    {:orientation :horizontal})
                  (pretty-properties/default-icon-props    {})
                  (pretty-properties/default-text-props    {:text-selectable? false})))
