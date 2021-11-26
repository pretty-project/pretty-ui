
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
  (merge {:autopop?         true
          :hide-animated?   true
          :initializer      [:ui/initialize-bubble! bubble-id]
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
      (if (some? timeout)
          {:dispatch-later [{:ms timeout :dispatch [:ui/destroy-element! :bubbles bubble-id]}]}
          [:ui/destroy-element! :bubbles bubble-id])))

(a/reg-event-fx
  :ui/blow-bubble!
  ; XXX#3044
  ; Ha a button elem nem jelenít meg {:content ...} tulajdonságként átadott tartalmat,
  ; akkor a primary-button a button elem bal oldalán jelenik meg.
  ;
  ; @param (keyword)(opt) bubble-id
  ; @param (map) bubble-props
  ;  {:autopop? (boolean)(opt)
  ;    Default: true
  ;   :content (metamorphic-content)(opt)
  ;    XXX#8711
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :success, :warning, :muted
  ;   :primary-button (map)(opt)
  ;   :update-animated? (boolean)(opt)
  ;    Default: false
  ;   :user-close? (boolean)(opt)
  ;    Default: true}
  ;
  ; @usage
  ;  [:ui/blow-bubble! {...}]
  ;
  ; @usage
  ;  [:ui/blow-bubble! :my-bubble {...}]
  (fn [{:keys [db]} event-vector]
      (let [bubble-id    (a/event-vector->second-id   event-vector)
            bubble-props (a/event-vector->first-props event-vector)
            bubble-props (a/prot bubble-id bubble-props bubble-props-prototype)]
           {:dispatch-if [(r bubbles-enabled-by-user? db)
                          [:ui/request-rendering-element! :bubbles bubble-id bubble-props]]})))



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
       [elements/button (assoc primary-button :on-click on-click)]))

(defn- bubble-close-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (component)
  [bubble-id _]
  [elements/button {:on-click [:ui/pop-bubble! bubble-id]
                    :preset   :close-icon-button}])

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
  ;  {:content (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [bubble-id {:keys [content] :as bubble-props}]
  [:div.x-app-bubbles--element--body
    (if (some? content)
        [bubble-content bubble-id bubble-props])
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
