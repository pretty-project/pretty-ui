
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.popup-b.views
    (:require [layouts.popup-b.helpers    :as helpers]
              [layouts.popup-b.prototypes :as prototypes]
              [re-frame.api               :as r]
              [reagent.api                :as reagent]
              [x.components.api           :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ; {:content (metamorphic-content)}
  [popup-id {:keys [content]}]
  [:div.popup-b--content [x.components/content popup-id content]])

(defn- popup-b
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ; {:close-by-cover? (boolean)(opt)}
  [popup-id {:keys [close-by-cover?] :as layout-props}]
  [:div.popup-b (helpers/layout-attributes popup-id layout-props)
                [:div.popup-b--cover (if close-by-cover? {:on-click #(r/dispatch [:x.ui/remove-popup! popup-id])})]
                [layout-structure popup-id layout-props]])

(defn layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ; {:close-by-cover? (boolean)(opt)
  ;  :content (metamorphic-content)
  ;  :on-mount (metamorphic-event)(opt)
  ;  :on-unmount (metamorphic-event)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [layout :my-popup {...}]
  [popup-id {:keys [on-mount on-unmount] :as layout-props}]
  (let [layout-props (prototypes/layout-props-prototype layout-props)]
       (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                            :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                            :reagent-render         (fn [_ _] [popup-b popup-id layout-props])})))
