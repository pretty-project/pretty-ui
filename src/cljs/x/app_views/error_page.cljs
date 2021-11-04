
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v0.8.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.error-page
    (:require [mid-fruits.candy   :refer [param]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def ERROR-CONTENT
     {:no-connection  {:title  :yo-do-not-have-internet-connection
                       :helper :please-check-your-internet-connection
                       :icon   :wifi_off}
      :page-not-found {:title  :page-is-not-available
                       :helper :the-link-you-followed-may-be-broken
                       :action :go-back!
                       :icon   :self_improvement}})



;; -- Converters --------------------------------------------------------------
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

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) content-props
  ;  {:action (keyword)(opt)
  ;    :go-back!, :go-to-main!
  ;   :helper (metamorphic-content)
  ;   :icon (keyword)(opt)
  ;   :title (metamorphic-content)}
  ;
  ; @return (component)
  [_ {:keys [action helper icon title]}]
  [:<> [elements/separator {:size :m}]
       (if (some? icon)
           [elements/icon {:icon icon
                           :size :xxl}])
       [elements/text {:content          title
                       :font-size        :xxl
                       :horizontal-align :center
                       :layout           :fit}]
       [elements/text {:content          helper
                       :font-size        :s
                       :horizontal-align :center
                       :layout           :fit}]
       (cond (= action :go-back!)
             [:<> [elements/separator {:orientation :horizontal :size :l}]
                  [elements/button {:label    :back!
                                    :variant  :transparent
                                    :on-click [:x.app-router/go-back!]}]])])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) error-id
  ;  :no-connection, :page-not-found, ...
  (fn [_ [_ error-id]]
      [:x.app-ui/set-surface!
       ::view
       {:content       #'view
        :content-props (error-id->content-props error-id)}]))

(a/reg-event-fx
  ::initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-router/add-route!
   :page-not-found
   {:route-event    [::render! :page-not-found]
    :route-template "/page-not-found"}])

(a/reg-lifecycles
  ::lifecycles
  ; Ha az error-handler {:on-app-init [...]} időzítéssel adja hozzá a :page-not-found
  ; route eseményt a rendszerhez, akkor azt lehetőség van {:on-app-boot [...]}
  ; időzítéssel felülírni.
  {:on-app-init [::initialize!]})
