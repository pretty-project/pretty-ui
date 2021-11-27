
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v1.2.2
; Compatibility: x4.4.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.error-page
    (:require [mid-fruits.candy   :refer [param]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def ERROR-CONTENT {:no-connection      {:title  :yo-do-not-have-internet-connection
                                         :helper :please-check-your-internet-connection
                                         :icon   :wifi_off}
                    :no-permission      {:title  :you-do-not-have-permission-to-view-this-page
                                         :icon   :warning_amber}
                    :page-not-found     {:title  :page-is-not-available
                                         :helper :the-link-you-followed-may-be-broken
                                         :icon   :self_improvement}
                    :under-construction {:title  :page-is-under-construction
                                         :icon   :self_improvement}
                    :under-maintenance  {:title  :page-is-under-maintenance
                                         :helper :please-check-back-soon...
                                         :icon   :self_improvement}})



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- error-id->content-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) error-id
  ;
  ; @return (map)
  ;  {:title (metamorphic-content)
  ;   :helper (metamorphic-content)}
  [error-id]
  (get ERROR-CONTENT error-id))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- error-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  ;  {:title (metamorphic-content)}
  ;
  ; @return (component)
  [_ {:keys [title]}]
  [elements/text ::error-title
                 {:content title :font-size :xxl :horizontal-align :center :layout :fit :selectable? false}])

(defn- error-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  ;  {:helper (metamorphic-content)}
  ;
  ; @return (component)
  [_ {:keys [helper]}]
  [elements/text ::error-helper
                 {:content helper :font-size :s :horizontal-align :center :layout :fit :selectable? false}])

(defn- error-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (component)
  [_ {:keys [icon]}]
  (if (some? icon)
      [elements/icon ::error-icon
                     {:icon icon :size :xxl}]))

(defn- go-back-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::go-back-button
                   {:label    :back!
                    :variant  :transparent
                    :on-click [:router/go-back!]}])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  ;
  ; @return (component)
  [surface-id content-props]
  [:<> [elements/separator {:size :xxl :orientation :horizontal}]
       [error-icon     surface-id content-props]
       [elements/separator {:orientation :horizontal :size :m}]
       [error-title    surface-id content-props]
       [error-helper   surface-id content-props]
       [elements/separator {:orientation :horizontal :size :m}]
       [go-back-button surface-id content-props]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/render-error-page!
  ; @param (keyword) error-id
  ;  :no-connection, :no-permission, :page-not-found, :under-construction, :under-maintenance
  (fn [_ [_ error-id]]
      [:ui/set-surface! ::view
                        {:content       #'view
                         :content-props (error-id->content-props error-id)}]))
