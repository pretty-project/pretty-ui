
(ns layouts.surface-a.views
    (:require [layouts.surface-a.helpers    :as surface-a.helpers]
              [layouts.surface-a.prototypes :as surface-a.prototypes]
              [re-frame.api                 :as r]
              [reagent.api                  :as reagent]
              [x.components.api             :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:content (metamorphic-content)}
  [surface-id {:keys [content] :as surface-props}]
  [:div#l-surface-a [:div#l-surface-a--body (surface-a.helpers/surface-body-attributes surface-id surface-props)
                                            [x.components/content                      surface-id content]]])

(defn layout
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:content (metamorphic-content)
  ;  :content-orientation (keyword)(opt)
  ;   :horizontal, :vertical
  ;   Default: :vertical
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :on-mount (metamorphic-event)(opt)
  ;  :on-unmount (metamorphic-event)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [surface-a :my-surface {...}]
  [surface-id {:keys [on-mount on-unmount] :as surface-props}]
  (let [layout-props (surface-a.prototypes/surface-props-prototype surface-props)]
       (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                            :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                            :reagent-render         (fn [_ _] [surface-a surface-id surface-props])})))
