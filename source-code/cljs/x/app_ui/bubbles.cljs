
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.21
; Description:
; Version: v2.0.6
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.bubbles
    (:require [mid-fruits.candy     :refer [param]]
              [mid-fruits.time      :as time]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-ui.element     :as element]
              [x.app-ui.renderer    :as renderer :rename {component renderer}]
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
                [:ui/pop-bubble! bubble-id]]})

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
  (merge {:autopop?    true
          :initializer [:ui/initialize-bubble! bubble-id]
          :user-close? true}
         (param bubble-props)
         {:hide-animated?   true
          :reveal-animated? true
          :update-animated? true}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bubbles-enabled-by-user?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (r user/get-user-settings-item db :notification-bubbles-enabled?))

(defn- bubble-lifetime-elapsed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ;
  ; @return (boolean)
  [db [_ bubble-id]]
  (let [render-requested-at (r renderer/get-render-log db :bubbles bubble-id :render-requested-at)]
       (> (time/elapsed)
          (+ render-requested-at BUBBLE-LIFETIME))))

(defn- get-bubble-lifetime-left
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ;
  ; @return (ms)
  [db [_ bubble-id]]
  (let [render-requested-at (r renderer/get-render-log db :bubbles bubble-id :render-requested-at)
        bubble-pop-time     (+ render-requested-at BUBBLE-LIFETIME)]
       (- (param bubble-pop-time)
          (time/elapsed))))

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
  :ui/initialize-bubble!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  (fn [{:keys [db]} [_ bubble-id]]
      {:dispatch-later [(if (r autopop-bubble? db bubble-id)
                            {:ms BUBBLE-LIFETIME :dispatch [:ui/autopop-bubble?! bubble-id]})]}))

(a/reg-event-fx
  :ui/autopop-bubble?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  (fn [{:keys [db]} [_ bubble-id]]
            ; Ha a bubble rendelkezik {:autopop? true} tulajdonsággal és lejárt az ideje,
            ; akkor megtörténik a pop esemény.
      (cond (and (r autopop-bubble?          db bubble-id)
                 (r bubble-lifetime-elapsed? db bubble-id))
            [:ui/pop-bubble! bubble-id]
            ; Ha a bubble rendelkezik {:autopop? true} tulajdonsággal és még NEM járt le az ideje,
            ; akkor beidőzít egy autopop eseményt a bubble várható élettartamára.
            ; Előfordulhat, hogy a bubble hátralévő idejében újra meghívásra kerül a blow esemény,
            ; ami miatt újraindul a bubble élettartama.
            (r autopop-bubble? db bubble-id)
            {:dispatch-later [{:ms       (r get-bubble-lifetime-left db bubble-id)
                               :dispatch [:ui/autopop-bubble?! bubble-id]}]})))

(a/reg-event-fx
  :ui/pop-bubble!
  ; @param (keyword) bubble-id
  ; @param (map)(opt) action-props
  ;  {:timeout (ms)(opt)
  ;    Default: 0}
  (fn [{:keys [db]} [_ bubble-id {:keys [timeout]}]]
      (if timeout {:dispatch-later [{:ms timeout :dispatch [:ui/destroy-element! :bubbles bubble-id]}]}
                  [:ui/destroy-element! :bubbles bubble-id])))

(a/reg-event-fx
  :ui/blow-bubble!
  ; @param (keyword)(opt) bubble-id
  ; @param (map) bubble-props
  ;  {:autopop? (boolean)(opt)
  ;    Default: true
  ;   :body (metamorphic-content)
  ;   :destructor (metamorphic-event)(opt)
  ;   :initializer (metamorphic-event)(opt)
  ;   :user-close? (boolean)(opt)
  ;    Default: true}
  ;
  ; @usage
  ;  [:ui/blow-bubble! {...}]
  ;
  ; @usage
  ;  [:ui/blow-bubble! :my-bubble {...}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ bubble-id bubble-props]]
      (let [bubble-props (bubble-props-prototype bubble-id bubble-props)]
           {:dispatch-if [(r bubbles-enabled-by-user? db)
                          [:ui/request-rendering-element! :bubbles bubble-id bubble-props]]})))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bubble-close-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;  {:user-close? (boolean)(opt)}
  ;
  ; @return (component)
  [bubble-id {:keys [user-close?]}]
  (if user-close? [elements/button {:on-click [:ui/pop-bubble! bubble-id]
                                    :preset   :close-icon-button}]))

(defn- bubble-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;  {:body (map)}
  ;
  ; @return (hiccup)
  [bubble-id {:keys [body]}]
  [:div.x-app-bubbles--element--body [components/content bubble-id body]])

(defn- bubble-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (hiccup)
  [bubble-id bubble-props]
  [:div (bubble-attributes   bubble-id bubble-props)
        [bubble-body         bubble-id bubble-props]
        [bubble-close-button bubble-id bubble-props]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [renderer :bubbles {:element               #'bubble-element
                      :max-elements-rendered MAX-BUBBLES-RENDERED
                      :queue-behavior        :wait
                      :rerender-same?        false}])
