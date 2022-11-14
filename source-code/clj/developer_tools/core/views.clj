
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.core.views
    (:require [developer-tools.core.styles            :as core.styles]
              [developer-tools.mongo-db-browser.views :as mongo-db-browser.views]
              [developer-tools.re-frame-browser.views :as re-frame-browser.views]
              [developer-tools.user-handler.views     :as user-handler.views]
              [mid-fruits.pretty                      :as pretty]
              [mid-fruits.string                      :as string]
              [mongo-db.api                           :as mongo-db]
              [re-frame.api                           :as r]
              [x.user.api                             :as x.user]))

              ; TEMP
              ;[docs.api :as docs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [menu-bar-style  (core.styles/menu-bar-style)
        menu-item-style (core.styles/menu-item-style)]
       (str "<div style=\""menu-bar-style"\">"
            "<a style=\""menu-item-style"\" href=\"/avocado-juice/re-frame\">Re-Frame</a>"
            "<a style=\""menu-item-style"\" href=\"/avocado-juice/mongo-db\">MongoDB</a>"
            "<a style=\""menu-item-style"\" href=\"/avocado-juice/users\">Users</a>"
            "</div>")))

(defn- developer-tools
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [uri] :as request}]
  (case uri "/avocado-juice" ""
            "/avocado-juice/mongo-db" (mongo-db-browser.views/view request)
            "/avocado-juice/re-frame" (re-frame-browser.views/view request)
            "/avocado-juice/users"    (user-handler.views/view     request)))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (if (or @(r/subscribe [:x.core/dev-mode?])
           (x.user/request->root-user? request))
      (let [body-style (core.styles/body-style)]
           (str "<html>"
                "<body style=\""body-style"\">"
                "<pre style=\"white-space: normal\">"
                (menu-bar        request)
                (developer-tools request)

                ; TEMP
                ;(docs/create-documentation! {:path "submodules/io-api"})

                "</pre>"
                "</body>"
                "</html>"))
      (str "<html><head></head><body>Permission denied!<br/></body></html>")))
