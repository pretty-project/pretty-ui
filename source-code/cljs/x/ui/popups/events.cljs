
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.popups.events
    (:require [re-frame.api  :as r :refer [r]]
              [x.ui.renderer :as renderer]))



;; -- Debug tools -------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stick-popup-to-top!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (map)
  [db [_ popup-id]]
  (r renderer/set-element-prop! db :popups popup-id :stick-to-top? true))

(defn minimize-popup!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (map)
  [db [_ popup-id]]
  (r renderer/set-element-prop! db :popups popup-id :minimized? true))

(defn maximize-popup!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (map)
  [db [_ popup-id]]
  (r renderer/set-element-prop! db :popups popup-id :minimized? false))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.ui/stick-popup-to-top! stick-popup-to-top!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.ui/minimize-popup! minimize-popup!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.ui/maximize-popup! maximize-popup!)
