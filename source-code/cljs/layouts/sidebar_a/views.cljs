
(ns layouts.sidebar-a.views
    (:require [layouts.sidebar-a.helpers    :as helpers]
              [layouts.sidebar-a.prototypes :as prototypes]
              [re-frame.api                 :as r]
              [react.api                    :as react]
              [reagent.api                  :as reagent]
              [x.components.api             :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) layout-props
  ; {:content (metamorphic-content)
  ;  :viewport-min (px)}
  [sidebar-id {:keys [content viewport-min] :as layout-props}]
  (if @(r/subscribe [:x.environment/viewport-min? viewport-min])
       [:div#l-sidebar-a (helpers/layout-attributes sidebar-id layout-props)
                         [:div#l-sidebar-a--content [x.components/content sidebar-id content]]]))

(defn layout
  ; @param (keyword) sidebar-id
  ; @param (map) layout-props
  ; {:content (metamorphic-content)
  ;  :on-mount (metamorphic-event)(opt)
  ;  :on-unmount (metamorphic-event)(opt)
  ;  :position (keyword)(opt)
  ;   :left, :right
  ;   Default: :left
  ;  :style (map)(opt)
  ;  :viewport-min (px)(opt)
  ;   Default: 720}
  ;
  ; @usage
  ; [layout :my-sidebar {...}]
  [sidebar-id {:keys [on-mount on-unmount] :as layout-props}]
  (let [layout-props (prototypes/layout-props-prototype layout-props)]
       (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                            :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                            :reagent-render         (fn [_ _] [sidebar-a sidebar-id layout-props])})))
