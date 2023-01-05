
(ns templates.item-handler.wrapper.views
    (:require [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- wrapper
  ; @param (keyword) handler-id
  ; @param (map) wrapper-props
  ; {:body (metamorphic content)
  ;  :footer (metamorphic content)
  ;  :header (metamorphic content)}
  [handler-id {:keys [body footer header]}]
  [:div#t-item-handler [x.components/content handler-id footer]
                       [x.components/content handler-id body]
                       [x.components/content handler-id header]])
