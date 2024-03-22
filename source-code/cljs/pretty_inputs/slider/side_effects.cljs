
(ns pretty-inputs.slider.side-effects
    (:require [dom.api                    :as dom]
              [pretty-inputs.slider.state :as slider.state]
              [re-frame.extra.api         :as r]
              [window.api                 :as window]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn slide!
  ; @ignore
  ;
  ; @param (DOM-event) mouse-event
  [mouse-event slider-id thumb-id]
  (let [initial-mouse-x     (get-in @slider.state/THUMBS [slider-id thumb-id :initial-mouse-x])
        initial-translate-x (get-in @slider.state/THUMBS [slider-id thumb-id :initial-translate-x])
        current-mouse-x     (dom/get-mouse-x mouse-event)
        current-translate-x (+ initial-translate-x (- current-mouse-x initial-mouse-x))]
       (swap! slider.state/THUMBS assoc-in [slider-id thumb-id :current-translate-x] current-translate-x)))

(defn stop-sliding!
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (keyword) thumb-id
  [mouse-event slider-id thumb-id]
  (swap! slider.state/THUMBS update-in [slider-id thumb-id] merge {;:start-position mouse-x
                                                                   :thumb-sliding? false}))

(defn start-sliding!
  ; @ignore
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
       (letfn [(sliding-f      [mouse-event] (slide!              mouse-event slider-id thumb-id))
               (stop-sliding-f [mouse-event] (dom/prevent-default mouse-event)
                                             (stop-sliding!       mouse-event slider-id thumb-id)
                                             (window/remove-event-listener! "mousemove" sliding-f)
                                             (window/remove-event-listener! "mouseup"   stop-sliding-f))]
              (dom/prevent-default mouse-event)
              (window/add-event-listener! "mousemove" sliding-f)
              (window/add-event-listener! "mouseup"   stop-sliding-f))))
