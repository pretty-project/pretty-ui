
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.sounds.side-effects
    (:require [app-fruits.dom         :as dom]
              [x.app-core.api         :as a]
              [x.app-ui.sounds.engine :as sounds.engine]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn play-sound!
  ; @param (keyword) sound-id
  ;
  ; @usage
  ;  (ui/play-sound! :my-sound)
  [sound-id]
  (let [catalog-id      (sounds.engine/sound-id->catalog-id sound-id)
        catalog-element (dom/get-element-by-id              catalog-id)]
       (.play catalog-element)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:ui/play-sound! :my-sound]
(a/reg-fx :ui/play-sound! play-sound!)