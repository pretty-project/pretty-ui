
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.29
; Description:
; Version: v1.4.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popup-layouts
    (:require [mid-fruits.keyword      :as keyword]
              [mid-fruits.map          :as map]
              [mid-fruits.vector       :as vector]
              [x.app-components.api    :as components]
              [x.app-core.api          :as a :refer [r]]
              [x.app-elements.api      :as elements]
              [x.app-environment.api   :as environment]
              [x.app-ui.element        :as element]
              [x.app-ui.popup-geometry :as geometry]
              [x.app-ui.renderer       :as renderer]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-props->cover-on-click-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) popup-props
  ;  {:user-close? (boolean)(opt)}
  ;
  ; @return (function)
  [{:keys [user-close?]}]
  (if user-close? #(a/dispatch [:ui/close-upper-popup!])))

(defn- popup-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:minimized? (boolean)(opt)
  ;   :stretched? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-minimized (boolean)
  ;   :data-stretched (boolean)}
  [popup-id {:keys [minimized? stretched?] :as popup-props}]
  (cond-> (element/element-attributes :popups popup-id popup-props
                                      {:data-nosnippet true})
          (some? stretched?) (assoc :data-stretched (boolean stretched?))
          (some? minimized?) (assoc :data-minimized (boolean minimized?))))

(defn- popup-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:autopadding? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-autopadding (boolean)}
  [_ {:keys [autopadding?]}]
  (merge {} (if (some? autopadding?)
                {:data-autopadding (boolean autopadding?)})))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-popup-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (map)
  ;  {:render-touch-anchor? (boolean)}
  [db [_ popup-id]]
  {:render-touch-anchor? false})
  ;:render-touch-anchor? (r geometry/render-touch-anchor? db popup-id)

(a/reg-sub :ui/get-popup-header-props get-popup-header-props)



;; -- Popup header components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-minimize-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:minimizable? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [popup-id {:keys [minimizable?]}]
  (if minimizable? [elements/button {:class    :x-app-popups--element--minimize-button
                                     :color    :invert
                                     :icon     :close_fullscreen
                                     :layout   :icon-button
                                     :on-click [:ui/minimize-popup! popup-id]
                                     :variant  :transparent}]))

(defn- popup-maximize-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:minimized? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [popup-id {:keys [minimized?]}]
  (if minimized? [elements/button {:class    :x-app-popups--element--maximize-button
                                   :color    :muted
                                   :icon     :fullscreen
                                   :layout   :icon-button
                                   :on-click [:ui/maximize-popup! popup-id]
                                   :variant  :filled}]))

(defn- popup-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:header (map)(opt)
  ;   :render-touch-anchor? (boolean)}
  ;
  ; @return (hiccup)
  [popup-id {:keys [header render-touch-anchor?] :as popup-props}]
  [:<> (if (boolean render-touch-anchor?) [:div.x-app-popups--element--touch-anchor])
       (if (some?   header)               [:div.x-app-popups--element--header [components/content popup-id header]]
                                          [:div.x-app-popups--element--header-placeholder])])

(defn popup-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (component)
  [popup-id popup-props]
  [components/subscriber popup-id
                         {:render-f   #'popup-header-structure
                          :base-props popup-props
                          :subscriber [:ui/get-popup-header-props popup-id]}])



;; -- Popup body components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:body (map)}
  ;
  ; @return (hiccup)
  [popup-id {:keys [body] :as popup-props}]
  [:div.x-app-popups--element--body (popup-body-attributes popup-id popup-props)
                                    [components/content    popup-id body]])



;; -- Popup layout components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-cover
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (hiccup)
  [_ popup-props]
  [:div.x-app-popups--element--cover
    {:on-click (popup-props->cover-on-click-event popup-props)}])

(defn- unboxed-popup-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (hiccup)
  [popup-id popup-props]
  [:div.x-app-popups--element--structure
    [popup-body   popup-id popup-props]
    [popup-header popup-id popup-props]])

(defn- boxed-popup-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (hiccup)
  [popup-id popup-props]
  [:div.x-app-popups--element--structure
    [:div.x-app-popups--element--background]
    [popup-header          popup-id popup-props]
    [popup-body            popup-id popup-props]
    [popup-minimize-button popup-id popup-props]])

(defn- popup-layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:layout (keyword)}
  ;
  ; @return (component)
  [popup-id {:keys [layout] :as popup-props}]
  (case layout :boxed   [boxed-popup-structure   popup-id popup-props]
               :unboxed [unboxed-popup-structure popup-id popup-props]))

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:minimized? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [popup-id {:keys [minimized?] :as popup-props}]
  [:<> [popup-maximize-button popup-id popup-props]
       [:div (popup-attributes popup-id popup-props)
             [popup-cover      popup-id popup-props]
             [popup-layout     popup-id popup-props]]])
