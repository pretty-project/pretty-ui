
; WARNING! NOT TESTED! DO NOT USE!

;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.components.slideshow
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.css            :as css]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]
              [x.app-environment.api     :as environment]
              [x.app-gestures.api        :as gestures]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

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
          :step-interval      5000
          :width              "100%"}
         (param slideshow-props)
         {:step-duration STEP-DURATION}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-slideshow-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) slideshow-id
  ;
  ; @return (map)
  [db [_ slideshow-id]]
  (merge (r engine/get-element-props   db slideshow-id)
         (r engine/get-steppable-props db slideshow-id)))

(a/reg-sub :elements/get-slideshow-props get-slideshow-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- slideshow-prev-step
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slideshow-id
  ; @param (map) slideshow-props
  ;  {:prev-step (map)
  ;    {:image-uri (string)}}
  [slideshow-id {:keys [prev-step]}]
  [:div.x-slideshow--prev-step {:style {:background-image (css/url (:image-uri prev-step))}}])

(defn- slideshow-current-step
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slideshow-id
  ; @param (map) slideshow-props
  ;  {:current-step (map)
  ;    {:image-uri (string)}}
  [slideshow-id {:keys [current-step]}]
  [:div.x-slideshow--current-step {:style {:background-image (css/url (:image-uri current-step))}}])

(defn- slideshow-temp-step
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slideshow-id
  ; @param (map) slideshow-props
  ;  {:current-step (map)
  ;    {:image-uri (string)}}
  [slideshow-id {:keys [current-step]}]
  [:div.x-slideshow--temp-step {:style {:background-image (css/url (:image-uri current-step))}}])

(defn- slideshow-next-step
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slideshow-id
  ; @param (map) slideshow-props
  ;  {:next-step (map)
  ;    {:image-uri (string)}}
  [slideshow-id {:keys [next-step]}]
  [:div.x-slideshow--next-step {:style {:background-image (css/url (:image-uri next-step))}}])

(defn- slideshow-steps
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slideshow-id
  ; @param (map) slideshow-props
  [slideshow-id slideshow-props]
  [:div.x-slideshow--steps [slideshow-prev-step    slideshow-id slideshow-props]
                           [slideshow-current-step slideshow-id slideshow-props]
                           [slideshow-next-step    slideshow-id slideshow-props]
                           [slideshow-temp-step    slideshow-id slideshow-props]])

(defn- slideshow-controls
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slideshow-id
  ; @param (map) slideshow-props
  [slideshow-id slideshow-props]
  [:div.x-slideshow--controls [:button.x-slideshow--controls--go-bwd
                                {:on-click    #(a/dispatch [:gestures/step-backward! slideshow-id])
                                 :on-mouse-up #(environment/blur-element!)}]
                              [:button.x-slideshow--controls--go-fwd
                                {:on-click    #(a/dispatch [:gestures/step-forward! slideshow-id])
                                 :on-mouse-up #(environment/blur-element!)}]])

(defn- slideshow
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slideshow-id
  ; @param (map) slideshow-props
  [slideshow-id slideshow-props]
  [:div.x-slideshow
    (engine/steppable-attributes slideshow-id slideshow-props)
    [slideshow-steps             slideshow-id slideshow-props]
    [slideshow-controls          slideshow-id slideshow-props]
    [:div {:style {:position "absolute" :top "-30px" :left 0}}

          (str (:current-dex    slideshow-props))
          (str (:step-direction slideshow-props))]])

          ; XXX#0000
          ;
          ; overflow-x: auto + set-scroll-x! = slideshow

(defn element
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
  ;   :step-interval (ms)(opt)
  ;    Default: 5000
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
  ([slideshow-props]
   [element (a/id) slideshow-props])

  ([slideshow-id slideshow-props]
   (let [slideshow-props    (slideshow-props-prototype                   slideshow-props)
         step-handler-props (gestures/extended-props->step-handler-props slideshow-props)]
        [engine/stated-element slideshow-id
                               {:render-f      #'slideshow
                                :element-props slideshow-props
                                :initializer   [:gestures/init-step-handler!  slideshow-id step-handler-props]
                                :subscriber    [:elements/get-slideshow-props slideshow-id]}])))
