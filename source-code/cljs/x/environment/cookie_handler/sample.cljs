
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.cookie-handler.sample
    (:require [re-frame.api      :as r :refer [r]]
              [x.environment.api :as x.environment]))



;; -- Sütik használata --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-subscription
  [db _]
  {:any-cookies-stored? (r x.environment/any-cookies-stored? db)
   :stored-cookies      (r x.environment/get-stored-cookies  db)
   :my-cookie-value     (r x.environment/get-cookie-value    db :my-cookie)})

(r/reg-event-fx :set-my-cookies!
  {:dispatch-n [[:x.environment/set-cookie! {}]
                [:x.environment/set-cookie! :my-cookie {}]]})

(r/reg-event-fx :remove-my-cookie!
  [:x.environment/remove-cookie! :my-cookie])

(r/reg-event-fx :remove-all-cookies!
  [:x.environment/remove-cookies!])
