
(ns pretty-elements.label.prototypes
    (:require [pretty-elements.properties.api :as pretty-elements.properties]))

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
  (-> label-props (pretty-elements.properties/default-border-props     {})
                  (pretty-elements.properties/default-content-props    {:placeholder "\u00A0"})
                  (pretty-elements.properties/default-font-props       {:font-size :s :font-weight :medium})
                  (pretty-elements.properties/default-flex-props       {:orientation :horizontal})
                  (pretty-elements.properties/default-label-icon-props {})
                  (pretty-elements.properties/default-text-props       {})))
