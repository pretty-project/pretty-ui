
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.developer-tools.views
    (:require [mid-fruits.pretty                         :as pretty]
              [mid-fruits.string                         :as string]
              [mongo-db.api                              :as mongo-db]
              [x.server-core.api                         :as a]
              [x.server-db.api                           :as db]
              [x.server-developer.developer-tools.styles :as developer-tools.styles]
              [x.server-developer.mongo-db-browser.views :as mongo-db-browser.views]
              [x.server-developer.re-frame-browser.views :as re-frame-browser.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [menu-bar-style  (developer-tools.styles/menu-bar-style)
        menu-item-style (developer-tools.styles/menu-item-style)]
       (str "<div style=\""menu-bar-style"\">"
            "<a style=\""menu-item-style"\" href=\"/developer-tools/re-frame-browser\">Re-Frame browser</a>"
            "<a style=\""menu-item-style"\" href=\"/developer-tools/mongo-db-browser\">MongoDB browser</a>"
            "</div>")))

(defn- developer-tools
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [uri] :as request}]
  (case uri "/developer-tools" ""
            "/developer-tools/mongo-db-browser" (mongo-db-browser.views/view request)
            "/developer-tools/re-frame-browser" (re-frame-browser.views/view request)))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (let [body-style (developer-tools.styles/body-style)]
       (str "<html>"
            "<body style=\""body-style"\">"
            "<pre style=\"white-space: normal\">"
            (menu-bar        request)
            (developer-tools request)
            "</pre>"
            "</body>"
            "</html>")))
