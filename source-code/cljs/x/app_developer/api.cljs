
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.api
    (:require [x.app-developer.developer-tools.effects]
              [x.app-developer.developer-tools.subs]
              [x.app-developer.developer-tools.views]
              [x.app-developer.effects]
              [x.app-developer.subs]
              [x.app-developer.request-inspector.events]
              [x.app-developer.request-inspector.subs]
              [x.app-developer.request-inspector.views]
              [x.app-developer.re-frame-browser.engine]
              [x.app-developer.re-frame-browser.events]
              [x.app-developer.re-frame-browser.subs]
              [x.app-developer.re-frame-browser.views]
              [x.app-developer.route-browser.views]
              [x.app-developer.database-screen.views :as database-screen.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-developer.database-screen.views
(def database-screen database-screen.views/view)
