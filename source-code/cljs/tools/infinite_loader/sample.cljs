
(ns tools.infinite-loader.sample
    (:require [re-frame.api              :as r]
              [tools.infinite-loader.api :as infinite-loader]))

;; -- How to use the component? -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-component
  []
  [infinite-loader/component :my-loader {:on-intersect [:my-event]
                                         :on-leave     [:your-event]}])

;; -- How to disable an infinite loader? --------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pause-my-loader!
  [:infinite-loader/pause-loader! :my-loader])

;; -- How to re-enable an infinite loader? ------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :restart-my-loader!
  [:infinite-loader/restart-loader! :my-loader])

;; -- How to reload an infinite loader? ---------------------------------------
;; ----------------------------------------------------------------------------

; By reloading the infinite loader, the actual callback event will be fired.

(r/reg-event-fx :reload-my-loader!
  [:infinite-loader/reload-loader! :my-loader])
