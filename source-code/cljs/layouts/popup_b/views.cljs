
(ns layouts.popup-b.views
    (:require [layouts.popup-b.prototypes :as popup-b.prototypes]
              [re-frame.api               :as r]
              [reagent.api                :as reagent]
              [x.components.api           :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:content (metamorphic-content)}
  [popup-id {:keys [content]}]
  [:div.l-popup-b--content [x.components/content popup-id content]])

(defn- popup-b
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:close-by-cover? (boolean)(opt)
  ;  :style (map)(opt)}
  [popup-id {:keys [close-by-cover? style] :as popup-props}]
  [:div.l-popup-b {:style style}
                  [:div.l-popup-b--cover (if close-by-cover? {:on-click #(r/dispatch [:x.ui/remove-popup! popup-id])})]
                  [popup-structure popup-id popup-props]])

(defn layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:close-by-cover? (boolean)(opt)
  ;  :content (metamorphic-content)
  ;  :on-mount (metamorphic-event)(opt)
  ;  :on-unmount (metamorphic-event)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [popup-b :my-popup {...}]
  [popup-id {:keys [on-mount on-unmount] :as popup-props}]
  (let [popup-props (popup-b.prototypes/popup-props-prototype popup-props)]
       (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                            :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                            :reagent-render         (fn [_ _] [popup-b popup-id popup-props])})))
