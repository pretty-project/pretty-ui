
(ns x.app-developer.api
    (:require [x.app-developer.developer-tools]
              [x.app-developer.engine]
              [x.app-developer.request-browser]
              [x.app-developer.source-reader]
              [x.app-developer.database-browser :as database-browser]
              [x.app-developer.database-screen  :as database-screen]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-developer.database-browser
(def database-browser database-browser/view)

; x.app-developer.database-screen
(def database-screen  database-screen/view)
