
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v1.9.4
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.bubbles
    (:require [mid-fruits.candy     :refer [param]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-ui.element     :as element]
              [x.app-ui.renderer    :as renderer :refer [view] :rename {view renderer}]
              [x.app-user.api       :as user]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def BUBBLE-LIFETIME 15000)

; @constant (integer)
(def MAX-BUBBLES-RENDERED 5)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- primary-button-on-click
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;  {:primary-button (map)
  ;    {:on-click (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:dispatch-n (vector)}
  [bubble-id {:keys [primary-button]}]
  {:dispatch-n [(:on-click primary-button)
                [:x.app-ui/pop-bubble! bubble-id]]})

(defn- bubble-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  [bubble-id bubble-props]
  (element/element-attributes :bubbles bubble-id bubble-props
                              {:data-nosnippet true}))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bubble-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  ;  {:autopop? (boolean)
  ;   :hide-animated? (boolean)
  ;   :initializer (metamorphic-event)
  ;   :reveal-animated? (boolean)
  ;   :update-animated? (boolean)
  ;   :user-close? (boolean)}
  [bubble-id bubble-props]
  (merge {:autopop?         true
          :hide-animated?   true
          :initializer      [:x.app-ui/initialize-bubble! bubble-id]
          :reveal-animated? true
          :update-animated? false
          :user-close?      true}
         (param bubble-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bubbles-enabled-by-user?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (r user/get-user-settings-item db :notification-bubbles-enabled?))

(defn- autopop-bubble?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ;
  ; @return (boolean)
  [db [_ bubble-id]]
  (boolean (r renderer/get-element-prop db :bubbles bubble-id :autopop?)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/initialize-bubble!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  (fn [{:keys [db]} [_ bubble-id]]
      (if (r autopop-bubble? db bubble-id)
          {:dispatch-later [{:ms BUBBLE-LIFETIME :dispatch [:x.app-ui/autopop-bubble?! bubble-id]}]})))

(a/reg-event-fx
  :x.app-ui/autopop-bubble?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  (fn [{:keys [db]} [_ bubble-id]]
      {:dispatch-if [(r autopop-bubble? db bubble-id)
                     [:x.app-ui/pop-bubble! bubble-id]]}))

(a/reg-event-fx
  :x.app-ui/pop-bubble!
  ; @param (keyword) bubble-id
  ; @param (map)(opt) action-props
  ;  {:timeout (ms)(opt)
  ;    Default: 0}
  (fn [{:keys [db]} [_ bubble-id {:keys [timeout]}]]
      (if (some? timeout)
          {:dispatch-later [{:ms timeout :dispatch [:x.app-ui/destroy-element! :bubbles bubble-id]}]}
          [:x.app-ui/destroy-element! :bubbles bubble-id])))

(a/reg-event-fx
  :x.app-ui/blow-bubble!
  ; @param (keyword)(opt) bubble-id
  ; @param (map) bubble-props
  ;  {:autopop? (boolean)(opt)
  ;    Default: true
  ;   :content (metamorphic-content)
  ;    XXX#8711
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :success, :warning, :muted
  ;   :primary-button (map)(opt)
  ;    {:icon (string)(opt)
  ;      Material icon class
  ;     :on-click (metamorphic-event)(opt)
  ;     :tooltip (metamorphic-content)(opt)}
  ;   :update-animated? (boolean)(opt)
  ;    Default: false
  ;   :user-close? (boolean)(opt)
  ;    Default: true}
  ;
  ; @usage
  ;  [:x.app-ui/blow-bubble! {...}]
  ;
  ; @usage
  ;  [:x.app-ui/blow-bubble! :my-bubble {...}]
  (fn [{:keys [db]} event-vector]
      (let [bubble-id    (a/event-vector->second-id   event-vector)
            bubble-props (a/event-vector->first-props event-vector)
            bubble-props (a/prot bubble-id bubble-props bubble-props-prototype)]
           {:dispatch-if [(r bubbles-enabled-by-user? db)
                          [:x.app-ui/render-element! :bubbles bubble-id bubble-props]]})))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bubble-primary-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;  {:primary-button (map)
  ;    {:icon (string)(opt)
  ;      Material icon class
  ;     :on-click (metamorphic-event)(opt)
  ;     :tooltip (metamorphic-content)(opt)}}
  ;
  ; @return (component)
  [bubble-id {:keys [primary-button] :as bubble-props}]
  (let [on-click (primary-button-on-click bubble-id bubble-props)]
       [elements/button (merge {:color   :none
                                :layout  :icon-button
                                :variant :transparent}
                               (param primary-button)
                               {:on-click on-click})]))

(defn- bubble-close-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (component)
  [bubble-id _]
  [elements/button {:color    :none
                    :icon     :close
                    :layout   :icon-button
                    :on-click [:x.app-ui/pop-bubble! bubble-id]
                    :tooltip  :close!
                    :variant  :transparent}])

(defn- bubble-close-button-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (hiccup)
  [_ _]
  [:div.x-app-bubbles--element--close-button-placeholder])

(defn- bubble-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (hiccup)
  [bubble-id bubble-props]
  [:div.x-app-bubbles--element--content
    [element/element-content bubble-id bubble-props]])

(defn- bubble-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;  {:primary-button (map)(opt)
  ;    XXX#0714
  ;   :user-close? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [bubble-id {:keys [primary-button user-close?] :as bubble-props}]
  [:div.x-app-bubbles--element--buttons
    (if (some? primary-button)
        [bubble-primary-button bubble-id bubble-props])
    (if (boolean user-close?)
        [bubble-close-button             bubble-id bubble-props]
        [bubble-close-button-placeholder bubble-id bubble-props])])

(defn- bubble-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (hiccup)
  [bubble-id bubble-props]
  [:div.x-app-bubbles--element--body
    [bubble-content bubble-id bubble-props]
    [bubble-buttons bubble-id bubble-props]])

(defn- bubble-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (hiccup)
  [bubble-id bubble-props]
  [:div (bubble-attributes bubble-id bubble-props)
        [bubble-body       bubble-id bubble-props]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [renderer :bubbles {:element               #'bubble-element
                      :max-elements-rendered MAX-BUBBLES-RENDERED
                      :queue-behavior        :wait
                      :rerender-same?        false}])
