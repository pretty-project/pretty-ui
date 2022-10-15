
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.cookie-handler.sample
    (:require [re-frame.api          :as r :refer [r]]
              [x.app-environment.api :as environment]))



;; -- Sütik használata --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-subscription
  [db _]
  {:any-cookies-stored? (r environment/any-cookies-stored? db)
   :stored-cookies      (r environment/get-stored-cookies  db)
   :my-cookie-value     (r environment/get-cookie-value    db :my-cookie)})

(r/reg-event-fx :set-my-cookies!
  {:dispatch-n [[:environment/set-cookie! {}]
                [:environment/set-cookie! :my-cookie {}]]})

(r/reg-event-fx :remove-my-cookie!
  [:environment/remove-cookie! :my-cookie])

(r/reg-event-fx :remove-all-cookies!
  [:environment/remove-cookies!])
