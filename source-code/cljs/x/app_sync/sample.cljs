
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.sample
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api :as a :refer [r]]
              [x.app-sync.api :as sync]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :send-my-request!
  [:sync/send-request! :my-request
                       {}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A {:validator-f ...} tulajdonságként átadott függvény paraméterként megkapja a szerver válaszát,
; és ha a függvény visszatérési értéke boolean típusként kiértékelve false érték, akkor az {:on-failure [...]}
; tulajdonságként átadott esemény történik meg (abban az esetben is ha a szerver válasza egyébként ezt nem indolkolná)
(a/reg-event-fx
  :send-my-validated-request!
  [:sync/send-request! :my-validated-request
                       {:uri "/my-uri"
                        :on-success []
                        :on-failure []
                        :validator-f string?}])

(defn your-response-valid?
  [db [_ request-id server-response]]
  (return true))

(a/reg-event-fx
  :send-your-validated-request!
  [:sync/send-request! :your-validated-request
                       {:uri "/your-uri"
                        :on-success []
                        :on-failure []
                        :validator-f #(r your-response-valid? db :your-validated-request %)}])
