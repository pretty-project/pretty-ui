
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.sounds.side-effects
    (:require [dom.api             :as dom]
              [re-frame.api        :as r]
              [x.ui.sounds.helpers :as sounds.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn play-sound!
  ; @param (keyword) sound-id
  ;
  ; @usage
  ;  (play-sound! :my-sound)
  [sound-id]
  (let [catalog-id      (sounds.helpers/sound-id->catalog-id sound-id)
        catalog-element (dom/get-element-by-id               catalog-id)]
       (.play catalog-element)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.ui/play-sound! :my-sound]
(r/reg-fx :x.ui/play-sound! play-sound!)
