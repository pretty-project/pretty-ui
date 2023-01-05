
(ns layouts.surface-a.views
    (:require [elements.api                 :as elements]
              [layouts.surface-a.helpers    :as helpers]
              [layouts.surface-a.prototypes :as prototypes]
              [re-frame.api                 :as r]
              [reagent.api                  :as reagent]
              [x.components.api             :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) layout-props
  ; {:content (metamorphic-content)}
  [surface-id {:keys [content] :as layout-props}]
  [:div#l-surface-a (helpers/layout-attributes surface-id layout-props)
                    [:div#l-surface-a--body (helpers/layout-body-attributes surface-id layout-props)
                                            [x.components/content surface-id content]]])

(defn layout
  ; @param (keyword) surface-id
  ; @param (map) layout-props
  ; {:content (metamorphic-content)
  ;  :content-orientation (keyword)(opt)
  ;   :horizontal, :vertical
  ;   Default: :vertical
  ;  :on-mount (metamorphic-event)(opt)
  ;  :on-unmount (metamorphic-event)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [layout :my-surface {...}]
  [surface-id {:keys [on-mount on-unmount] :as layout-props}]
  (let [layout-props (prototypes/layout-props-prototype layout-props)]
       (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                            :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                            :reagent-render         (fn [_ _] [surface-a surface-id layout-props])})))
