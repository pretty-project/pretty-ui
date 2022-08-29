
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.keypress-handler.subs
    (:require [mid-fruits.map :as map]
              [x.app-core.api :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-pressed-keys
  ; @usage
  ;  (r environment/get-pressed-keys? db)
  ;
  ; @return (integers in vector)
  [db _]
  (keys (get-in db [:environment :keypress-handler/meta-items :pressed-keys])))

(defn key-pressed?
  ; @param (integer) key-code
  ;
  ; @usage
  ;  (r environment/key-pressed? db 27)
  ;
  ; @return (boolean)
  [db [_ key-code]]
  (boolean (get-in db [:environment :keypress-handler/meta-items :pressed-keys key-code])))

(defn type-mode-enabled?
  ; @usage
  ;  (r environment/type-mode-enabled? db)
  ;
  ; @return (boolean)
  [db _]
  (boolean (get-in db [:environment :keypress-handler/meta-items :type-mode?])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn keypress-prevented-by-event?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (boolean)
  [db [_ event-id]]
  (let [prevent-default? (get-in db [:environment :keypress-handler/data-items event-id :prevent-default?])]
       (boolean prevent-default?)))

(defn keypress-prevented-by-other-events?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (boolean)
  [db [_ event-id]]
  (let [keypress-events (get-in db [:environment :keypress-handler/data-items])
        other-events    (dissoc keypress-events event-id)]
       (map/any-key-match? other-events #(r keypress-prevented-by-event? db %))))

(defn enable-default?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (boolean)
  [db [_ event-id]]
  ; Enable default if prevented by event and NOT prevented by other events ...
  (and      (r keypress-prevented-by-event?        db event-id)
       (not (r keypress-prevented-by-other-events? db event-id))))

(defn get-on-keydown-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  ;
  ; @return (vector)
  [db [_ key-code]]
  (letfn [(f [keydown-events event-id] (conj keydown-events (get-in db [:environment :keypress-handler/data-items event-id :on-keydown])))]
         (let [keydown-event-ids (get-in db [:environment :keypress-handler/meta-items :cache key-code :keydown-events])]
              (reduce f [] keydown-event-ids))))

(defn get-on-keyup-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) key-code
  ;
  ; @return (vector)
  [db [_ key-code]]
  (letfn [(f [keyup-events event-id] (conj keyup-events (get-in db [:environment :keypress-handler/data-items event-id :on-keyup])))]
         (let [keyup-event-ids (get-in db [:environment :keypress-handler/meta-items :cache key-code :keyup-events])]
              (reduce f [] keyup-event-ids))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:environment/key-pressed? 27]
(a/reg-sub :environment/key-pressed? key-pressed?)
