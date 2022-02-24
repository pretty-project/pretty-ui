
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns playground.events
    (:require [x.app-core.api :as a :refer [r]]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (assoc-in db [:playground :stuff/data-items :stored-options]
               [{:x "Apple"}
                {:x "Apple juice"}
                {:x "Pineapple"}
                {:x "Banana"}
                {:x "Brown"}
                {:x "Apocalypse"}]))

(defn initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (as-> db % (r store-options! %)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :playground/initialize! initialize!)
