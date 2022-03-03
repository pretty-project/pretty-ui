
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.settings.privacy-settings.views
    (:require [app-extensions.settings.cookie-settings.views :rename {body cookie-settings}]
              [x.app-elements.api                            :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [cookie-settings])
