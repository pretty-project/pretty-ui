
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.scroll-handler.helpers
    (:require [dom.api                                    :as dom]
              [re-frame.api                               :as r]
              [x.environment.element-handler.side-effects :as element-handler.side-effects]
              [x.environment.scroll-handler.config        :as scroll-handler.config]
              [x.environment.scroll-handler.state         :as scroll-handler.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scroll-listener
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (DOM-event) event
  [_]
  (let [scrolled-to-top? (<= (dom/get-scroll-y) scroll-handler.config/SCROLLED-TO-TOP-THRESHOLD)]
       (if (not= @scroll-handler.state/SCROLLED-TO-TOP? scrolled-to-top?)
           ; If scrolled-to-top? changed ...
           (do (element-handler.side-effects/set-element-attribute! "x-body-container" "data-scrolled-to-top" scrolled-to-top?)
               (reset! scroll-handler.state/SCROLLED-TO-TOP? scrolled-to-top?)))))
              ;(r/dispatch-sync [:x.db/set-item! [:x.environment :sroll-handler/meta-items :scrolled-to-top?] scrolled-to-top?])