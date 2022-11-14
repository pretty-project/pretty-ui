
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.progress-bar.subs
    (:require [candy.api    :refer [return]]
              [re-frame.api :as r :refer [r]]
              [x.core.api   :as x.core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn process-faked?
  ; @usage
  ;  (r process-faked? db)
  ;
  ; @return (boolean)
  [db _]
  (get-in db [:x.ui :progress-bar/meta-items :fake-progress]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-process-progress
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (integer)
  [db _]
  (let [fake-progress (get-in db [:x.ui :progress-bar/meta-items :fake-progress] 0)]
       (if-let [process-id (get-in db [:x.ui :progress-bar/meta-items :process-id])]
               (let [process-progress (r x.core/get-process-progress db process-id)]
                    (max fake-progress process-progress))
               (return fake-progress))))

(defn process-failured?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (if-let [process-id (get-in db [:x.ui :progress-bar/meta-items :process-id])]
          (r x.core/process-failured? db process-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.ui/process-faked?]
(r/reg-sub :x.ui/process-faked? process-faked?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :x.ui/get-process-progress get-process-progress)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :x.ui/process-failured? process-failured?)
