
(ns templates.item-handler.footer.views
    (:require [templates.item-handler.footer.helpers :as footer.helpers]
              [x.components.api                      :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer
  ; @param (keyword) handler-id
  ; @param (map) footer-props
  ;
  ; @usage
  ; [footer :my-handler {...}]
  [handler-id footer-props]
  (let [item-info-label (footer.helpers/get-item-info-label handler-id footer-props)]
       [:div#t-item-handler--footer [:div#t-item-handler--footer-content {:data-color       :muted
                                                                          :data-font-size   :xxs
                                                                          :data-font-weight :medium
                                                                          :data-line-height :text-block
                                                                          :data-selectable  false}
                                                                         (x.components/content item-info-label)]]))
