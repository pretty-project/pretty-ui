
(ns components.notification-bubble.views
    (:require [elements.api                              :as elements]
              [components.notification-bubble.helpers    :as notification-bubble.helpers]
              [components.notification-bubble.prototypes :as notification-bubble.prototypes]
              [random.api                                :as random]
              [re-frame.api                              :as r]
              [x.components.api                          :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn primary-button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:indent (map)
  ;  :preset (keyword)}
  [button-props]
  (merge {:indent {:bottom :xxs :vertical :xs}
          :preset :primary}
      button-props))

(defn state-changed-bubble-body
  ; @param (keyword) bubble-id
  ; @param (map) body-props
  ; {:label (metamorphic-content)(opt)
  ;  :primary-button (map)(opt)
  ;   {:label (metamorphic-content)
  ;    :on-click (metamorphic-event)}}
  ;
  ; @usage
  ; [state-changed-bubble-body :my-bubble {...}]
  [bubble-id {:keys [label primary-button]}]
  [:<> (if label          [elements/label  {:content label :indent {:all :xs}}])
       (if primary-button [elements/button (primary-button-props-prototype primary-button)])])

;; -- Bubble components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-close-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  [bubble-id]
  (if-let [user-close? @(r/subscribe [:x.ui.renderer/get-element-prop :bubbles bubble-id :user-close?])]
          [elements/icon-button {:hover-color :highlight
                                 :on-click    [:x.ui/remove-bubble! bubble-id]
                                 :preset      :close}]))

(defn bubble-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  [bubble-id]
  ; - Az egyes bubble elemeken megjelenített egyszerű szöveges tartalom (keyword vagy string típus)
  ;  {:indent {:vertical :xs}} beállítással jelenik meg.
  ;
  ; - Az egyes bubble elemeken megjelenített ...
  ;  ... komponensek közvetlenül a bubble elem szélétől jelennek meg (esztétikai távolság nélkül).
  ;  ... szöveges tartalmak esztétikai távolság alkalmazásával jelennek meg.
  ;
  ; - Az egyes bubble elemeken megjelenített komponenseket azért szükséges közvetlenül a bubble elem
  ;  szélétől megjeleníteni, hogy a komponensben megjelenített (és a komponens jobb szélére igazított)
  ;  ikon-gombok a bubble elem saját bubble-close-icon-button komponensével megegyezően a bubble elem
  ;  széléhez igazítva jelenjenek meg.
  (let [body @(r/subscribe [:x.ui.renderer/get-element-prop :bubbles bubble-id :body])]
       [:div.x-app-bubbles--element--body
         (cond (keyword? body) [elements/label bubble-id {:content body :indent {:vertical :xs}}]
               (string?  body) [elements/label bubble-id {:content body :indent {:vertical :xs}}]
               (map?     body) [elements/label bubble-id (merge {:indent {:vertical :xs}} body)]
               :default        [x.components/content bubble-id body])]))








(defn- notification-bubble
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:style (map)(opt)}
  [bubble-id {:keys [style] :as bubble-props}]
  [:div.c-notification-bubble {:style style}])

(defn component
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {:border-color (keyword)(opt)
  ;   :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
  ;  :border-radius (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :xxs
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :indent (map)(opt)
  ;  :min-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :outdent (map)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [notification-bubble {...}]
  ;
  ; @usage
  ; [notification-bubble :my-notification-bubble {...}]
  ([bubble-props]
   [component (random/generate-keyword) bubble-props])

  ([bubble-id bubble-props]
   (let [bubble-props (notification-bubble.prototypes/bubble-props-prototype bubble-props)]
        [notification-bubble bubble-id bubble-props])))
