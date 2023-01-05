
(ns elements.element.views
    (:require [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-badge
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:badge-color (keyword or string)(opt)
  ;  :badge-content (metamorphic-content)(opt)}
  [_ {:keys [badge-color badge-content]}]
  ; A {:badge-content ...} tulajdonság használható, a {:badge-color ...} tulajdonság meghatározása
  ; nélkül is!
  (cond (and badge-color badge-content)
        [:div.e-element-badge {:data-badge-color badge-color}
                              [:div.e-element-badge--content {:data-font-size   :xxs
                                                              :data-font-weight :extra-bold}
                                                             (x.components/content badge-content)]]
        badge-color   [:div.e-element-badge {:data-badge-color badge-color}]
        badge-content [:div.e-element-badge {:data-badge-color :primary}
                                            [:div.e-element-badge--content {:data-font-size   :xxs
                                                                            :data-font-weight :extra-bold}
                                                                           (x.components/content badge-content)]]))
