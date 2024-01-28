
(ns pretty-engine.element.timeout.props
    (:require [countdown-timer.api               :as countdown-timer]
              [pretty-engine.element.timeout.env :as element.timeout.env]
              [time.api                          :as time]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-timeout-props
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {}
  ;
  ; @return (map)
  [element-id {:keys [on-click-f on-click-timeout] :as element-props}]
  (letfn [(f0 [] (countdown-timer/start-countdown! element-id {:step 1000 :timeout on-click-timeout :on-start-f on-click-f}))]
         (if-let [element-timeout-left (element.timeout.env/get-element-timeout-left element-id element-props)]
                 (-> element-props (assoc :disabled?  (-> true)
                                          :content    (-> element-timeout-left time/ms->s (str "s"))
                                          :label      (-> element-timeout-left time/ms->s (str "s"))
                                          :on-click-f (if on-click-timeout f0 on-click-f)))
                 (-> element-props (assoc :on-click-f (if on-click-timeout f0 on-click-f))))))
