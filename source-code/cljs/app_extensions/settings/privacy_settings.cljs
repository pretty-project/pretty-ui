
(ns app-extensions.settings.privacy-settings
    (:require [x.app-elements.api :as elements]
              [app-extensions.settings.cookie-settings :rename {body cookie-settings}]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [cookie-settings])
