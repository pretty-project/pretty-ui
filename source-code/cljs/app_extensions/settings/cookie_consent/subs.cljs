
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.settings.cookie-consent.subs
    (:require [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]
              [x.app-ui.api          :as ui]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- render-consent?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (and      (r ui/application-interface?              db)
       (not (r environment/necessary-cookies-enabled? db))))
