
(ns x.app-environment.cookie-handler.sample
    (:require [x.app-core.api        :as a]
              [x.app-environment.api :as environment]))



;; -- Sütik használata --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-subscription
  [db _]
  {:any-cookies-stored? (r environment/any-cookies-stored? db)
   :stored-cookies      (r environment/get-stored-cookies  db)
   :my-cookie-value     (r environment/get-cookie-value    db :my-cookie)})

(a/reg-event-fx
  :set-my-cookies!
  {:dispatch-n [[:environment/set-cookie! {}]
                [:environment/set-cookie! :my-cookie {}]]})

(a/reg-event-fx
  :remove-my-cookie!
  [:environment/remove-cookie! :my-cookie])

(a/reg-event-fx
  :remove-all-cookies!
  [:environment/remove-cookies!])
