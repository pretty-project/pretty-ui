
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.badge-handler.views
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-badge
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:badge-color (keyword)(opt)
  ;   :badge-content (metamorphic-content)(opt)}
  [_ {:keys [badge-color badge-content]}]
  ; A {:badge-content ...} tulajdonság használható, a {:badge-color ...} tulajdonság meghatározása
  ; nélkül is!
  (cond (and badge-color badge-content)
        [:div.x-element-badge {:data-color badge-color}
                              [:div.x-element-badge--content (components/content badge-content)]]
        badge-color   [:div.x-element-badge {:data-color badge-color}]
        badge-content [:div.x-element-badge {:data-color :primary}
                                            [:div.x-element-badge--content (components/content badge-content)]]))
