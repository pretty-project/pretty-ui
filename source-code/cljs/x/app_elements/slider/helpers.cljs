
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.slider.helpers
    (:require [dom.api                        :as dom]
              [mid-fruits.css                 :as css]
              [x.app-core.api                 :as a]
              [x.app-elements.element.helpers :as element.helpers]
              [x.app-elements.slider.state    :as slider.state]
              [x.app-environment.api          :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn slider-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ;  {}
  ;
  ; @return (function)
  [slider-id {:keys [initial-value] :as slider-props}]
  #(if initial-value (a/dispatch [:elements.slider/slider-did-mount slider-id slider-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sliding!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-event) mouse-event
  [mouse-event slider-id thumb-id]
  (let [start-x (get-in @slider.state/THUMBS [thumb-id :start-x])
        mouse-x (dom/get-mouse-x mouse-event)]
       (swap! slider.state/THUMBS assoc-in [slider-id thumb-id :translate-x] 50))
  (println (dom/get-mouse-x mouse-event)))

(defn stop-sliding!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (keyword) thumb-id
  [mouse-event slider-id thumb-id]
  (swap! slider.state/THUMBS update-in [slider-id thumb-id] merge {;:start-position mouse-x
                                                                   :sliding?       false}))

(defn start-sliding!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-event) mouse-event
  ; @param (keyword) slider-id
  ; @param (keyword) thumb-id
  [mouse-event slider-id thumb-id]
  (let [mouse-x (dom/get-mouse-x mouse-event)]
       (swap! slider.state/THUMBS update-in [slider-id thumb-id] merge {:start-x  mouse-x
                                                                        :sliding? true})
       (letfn [(sliding-f      [mouse-event] (sliding!             mouse-event slider-id thumb-id))
               (stop-sliding-f [mouse-event] (dom/prevent-default! mouse-event)
                                             (stop-sliding!        mouse-event slider-id thumb-id)
                                             (dom/remove-event-listener! "mousemove" sliding-f)
                                             (dom/remove-event-listener! "mouseup"   stop-sliding-f))]
              (dom/prevent-default! mouse-event)
              (dom/add-event-listener! "mousemove" sliding-f)
              (dom/add-event-listener! "mouseup"   stop-sliding-f))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn slider-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [slider-id {:keys [] :as slider-props}]
  (merge (element.helpers/element-default-attributes slider-id slider-props)
         (element.helpers/element-indent-attributes  slider-id slider-props)
         {}))

(defn slider-primary-thumb-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [slider-id {:keys []}]
  (let [translate-x (get-in @slider.state/THUMBS [slider-id :primary :translate-x] 0)]
       {:data-clickable true
        :on-mouse-down  #(start-sliding! % slider-id :primary)
        :style          {:left      (->         100 css/percent)
                         :transform (-> translate-x css/percent css/translate-x)}}))

(defn slider-secondary-thumb-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [slider-id slider-props]
  (let [translate-x (get-in @slider.state/THUMBS [slider-id :secondary :translate-x] 0)]
       {:data-clickable true
        :on-mouse-down  #(start-sliding! % slider-id :secondary)
        :style          {:left      (->           0 css/percent)
                         :transform (-> translate-x css/percent css/translate-x)}}))

(defn slider-line-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [slider-id slider-props]
  (let []
       {:data-clickable true
        :style {:width "200px"}}))
