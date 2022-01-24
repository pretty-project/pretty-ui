
(ns x.app-sync.sample
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-sync.api :as sync]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :send-my-request!
  [:sync/send-request! :my-request
                       {}])
