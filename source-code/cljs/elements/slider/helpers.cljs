
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.slider.helpers
    (:require [css.api                  :as css]
              [dom.api                  :as dom]
              [elements.element.helpers :as element.helpers]
              [elements.slider.state    :as slider.state]
              [re-frame.api             :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn slider-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ;  {:initial-value (vector)(opt)}
  [slider-id {:keys [initial-value] :as slider-props}]
  (if initial-value (r/dispatch [:elements.slider/slider-did-mount slider-id slider-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn slide!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-event) mouse-event
  [mouse-event slider-id thumb-id]
  (let [initial-mouse-x     (get-in @slider.state/THUMBS [slider-id thumb-id :initial-mouse-x])
        initial-translate-x (get-in @slider.state/THUMBS [slider-id thumb-id :initial-translate-x])
        current-mouse-x     (dom/get-mouse-x mouse-event)
        current-translate-x (+ initial-translate-x (- current-mouse-x initial-mouse-x))]
       (swap! slider.state/THUMBS assoc-in [slider-id thumb-id :current-translate-x] current-translate-x)))

(defn stop-sliding!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) slider-id
  ; @param (keyword) thumb-id
  [mouse-event slider-id thumb-id]
  (swap! slider.state/THUMBS update-in [slider-id thumb-id] merge {;:start-position mouse-x
                                                                   :thumb-sliding? false}))

(defn start-sliding!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-event) mouse-event
  ; @param (keyword) slider-id
  ; @param (keyword) thumb-id
  [mouse-event slider-id thumb-id]
  (let [initial-mouse-x     (dom/get-mouse-x mouse-event)
        initial-translate-x (get-in @slider.state/THUMBS [slider-id thumb-id :current-translate-x] 0)]
       (swap! slider.state/THUMBS update-in [slider-id thumb-id] merge {:initial-mouse-x     initial-mouse-x
                                                                        :initial-translate-x initial-translate-x
                                                                        :thumb-sliding?      true})
       (letfn [(sliding-f      [mouse-event] (slide!               mouse-event slider-id thumb-id))
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
  ;
  ; @return (map)
  [slider-id slider-props]
  (merge (element.helpers/element-default-attributes slider-id slider-props)
         (element.helpers/element-indent-attributes  slider-id slider-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (let [translate-x (get-in @slider.state/THUMBS [slider-id :primary :current-translate-x] 0)]
       {:data-clickable true
        :on-mouse-down  #(start-sliding! % slider-id :primary)
        :style          {:left      (->           0 css/px)
                         :transform (-> translate-x css/px css/translate-x)}}))

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
  (let [translate-x (get-in @slider.state/THUMBS [slider-id :secondary :current-translate-x] 0)]
       {:data-clickable true
        :on-mouse-down  #(start-sliding! % slider-id :secondary)
        :style          {:right     (->           0 css/px)
                         :transform (-> translate-x css/px css/translate-x)}}))

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
