
(ns extensions.settings.privacy-settings
    (:require [x.app-elements.api :as elements]
              [extensions.settings.cookie-settings :rename {view cookie-settings}]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [cookie-settings body-id])
