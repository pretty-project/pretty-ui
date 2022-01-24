
(ns x.mid-core.sample
    (:require [x.mid-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Ha a debug! interceptort használod, akkor az esemény megtörténésekor az esemény-vektor
; kiíródik a console/terminálra.
(a/reg-event-fx :my-event [a/debug!] (fn [_ _] [:do-something!]))

; Ha a self-destruct! interceptort használod, akkor az első esemény megtörténése után törlődik
; az esemény-kezelőből.
(a/reg-event-fx :your-event [a/self-destruct!] (fn [_ _] [:do-something!]))
