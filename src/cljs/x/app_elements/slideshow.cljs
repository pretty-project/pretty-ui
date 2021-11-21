
; WARNING! NOT TESTED! DO NOT USE!



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.09
; Description:
; Version: v0.1.8
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.slideshow
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.css            :as css]
              [mid-fruits.loop           :refer [reduce-indexed]]
              [mid-fruits.vector         :as vector]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]
              [x.app-gestures.api        :as gestures]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def DEFAULT-STEP-INTERVAL 5000)

; @constant (ms)
(def STEP-DURATION 500)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- slideshow-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) slideshow-props
  ;
  ; @return (map)
  ;  {:autostep? (boolean)
  ;   :height (string)
  ;   :infinite-stepping? (boolean)
  ;   :step-animation (keyword)
  ;   :step-duration (integer)
  ;   :step-interval (integer)
  ;   :width (string)}
  [slideshow-props]
  (merge {:autostep?          true
          :height             "100%"
          :infinite-stepping? true
          :step-animation     :slide-in
          :step-interval      DEFAULT-STEP-INTERVAL
          :width              "100%"}
         (param slideshow-props)
         {:step-duration STEP-DURATION}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) slideshow-id
  ;
  ; @return (map)
  [db [_ slideshow-id]]
  (merge (r engine/get-element-view-props   db slideshow-id)
         (r engine/get-steppable-view-props db slideshow-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- slideshow-prev-step
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slideshow-id
  ; @param (map) view-props
  ;  {:prev-step (map)
  ;    {:image-uri (string)}}
  ;
  ; @return (hiccup)
  [slideshow-id {:keys [prev-step]}]
  [:div.x-slideshow--prev-step
    {:style {:background-image (css/url (:image-uri prev-step))}}])

(defn- slideshow-current-step
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slideshow-id
  ; @param (map) view-props
  ;  {:current-step (map)
  ;    {:image-uri (string)}}
  ;
  ; @return (hiccup)
  [slideshow-id {:keys [current-step]}]
  [:div.x-slideshow--current-step
    {:style {:background-image (css/url (:image-uri current-step))}}])

(defn- slideshow-temp-step
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slideshow-id
  ; @param (map) view-props
  ;  {:current-step (map)
  ;    {:image-uri (string)}}
  ;
  ; @return (hiccup)
  [slideshow-id {:keys [current-step]}]
  [:div.x-slideshow--temp-step
    {:style {:background-image (css/url (:image-uri current-step))}}])

(defn- slideshow-next-step
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slideshow-id
  ; @param (map) view-props
  ;  {:next-step (map)
  ;    {:image-uri (string)}}
  ;
  ; @return (hiccup)
  [slideshow-id {:keys [next-step]}]
  [:div.x-slideshow--next-step
    {:style {:background-image (css/url (:image-uri next-step))}}])

(defn- slideshow-steps
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slideshow-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [slideshow-id view-props]
  [:div.x-slideshow--steps
    [slideshow-prev-step    slideshow-id view-props]
    [slideshow-current-step slideshow-id view-props]
    [slideshow-next-step    slideshow-id view-props]
    [slideshow-temp-step    slideshow-id view-props]])

(defn- slideshow-controls
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slideshow-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [slideshow-id view-props]
  [:div.x-slideshow--controls
    [:button.x-slideshow--controls--go-bwd
      {:on-click   #(a/dispatch [:gestures/step-backward! slideshow-id])
       :on-mouse-up (engine/blur-element-function slideshow-id)}]
    [:button.x-slideshow--controls--go-fwd
      {:on-click   #(a/dispatch [:gestures/step-forward! slideshow-id])
       :on-mouse-up (engine/blur-element-function slideshow-id)}]])

(defn- slideshow
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slideshow-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [slideshow-id view-props]
  [:div.x-slideshow
    (engine/steppable-attributes slideshow-id view-props)
    [slideshow-steps    slideshow-id view-props]
    [slideshow-controls slideshow-id view-props]
    [:div {:style {:position "absolute" :top "-30px" :left 0}}

          (str (:current-dex view-props))
          (str (:step-direction view-props))]])

          ; XXX#0000
          ;
          ; overflow-x: auto + set-scroll-x! = slideshow

(defn view
  ; @param (keyword)(opt) slideshow-id
  ; @param (map) slideshow-props
  ;  {:autostep? (boolean)(opt)
  ;    Default: true
  ;   :height (string)(opt)
  ;    "*px", "*%"
  ;    Default: "100%"
  ;   :infinite-stepping? (boolean)(opt)
  ;    Default: true
  ;   :steps (maps in vector)
  ;    [{:image-uri (string)}]
  ;   :step-animation (keyword)(opt)
  ;    :slide-in
  ;    Default: :slide-in
  ;   :step-interval (integer)(opt)
  ;    (ms)
  ;    Default: DEFAULT-STEP-INTERVAL
  ;    Only w/ {:autostep? true}
  ;   :width (string)(opt)
  ;    "*px", "*%"
  ;    Default: "100%"}
  ;
  ; @usage
  ;  [elements/slideshow {...}]
  ;
  ; @usage
  ;  [elements/slideshow :my-slideshow {...}]
  ;
  ; @return (component)
  ([slideshow-props]
   [view nil slideshow-props])

  ([slideshow-id slideshow-props]
   (let [slideshow-id       (a/id   slideshow-id)
         slideshow-props    (a/prot slideshow-props slideshow-props-prototype)
         step-handler-props (gestures/extended-props->step-handler-props slideshow-props)]
        [engine/stated-element slideshow-id
          {:component     #'slideshow
           :element-props slideshow-props
           :initializer   [:gestures/init-step-handler! slideshow-id step-handler-props]
           :subscriber    [::get-view-props             slideshow-id]}])))
