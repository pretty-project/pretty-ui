
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.docs.views
    (:require [developer-tools.docs.styles :as docs.styles]
              [docs.api                    :as docs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- refresh-page
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [status]
  (str "<script type=\"text/javascript\">window.location.href='?status="status"'</script>"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- generate-docs
  [{:keys [query-params]}]
  (if (and (docs/create-documentation! {:path "submodules/ajax-api"})
           (docs/create-documentation! {:path "submodules/bybit-api"})
           (docs/create-documentation! {:path "submodules/cljc-fruits"})
           (docs/create-documentation! {:path "submodules/docs-api"})
           (docs/create-documentation! {:path "submodules/dom-api"})
           (docs/create-documentation! {:path "submodules/io-api"})
           (docs/create-documentation! {:path "submodules/local-db-api"})
           (docs/create-documentation! {:path "submodules/logger-api"})
           (docs/create-documentation! {:path "submodules/mongo-db-api"})
           (docs/create-documentation! {:path "submodules/re-frame-api"})
           (docs/create-documentation! {:path "submodules/time-api"})
           (docs/create-documentation! {:path "submodules/window-api"}))
      (refresh-page "successed")
      (refresh-page "failured")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- generate-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [menu-button-style (docs.styles/menu-button-style)]
       (str "<a style=\""menu-button-style"\" href=\"/avocado-juice/docs?generate-docs=true\">Generate docs</a>")))

(defn- status
  [{:keys [query-params]}]
  (let [status (get query-params "status")]
       (case status "successed" "</br></br><div style=\"color:green;\">Docs generated</div>"
                    "failured"  "</br></br><div style=\"color:red;\">  Failure</div>"
                                "<div></div>")))

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [menu-bar-style (docs.styles/menu-bar-style)]
       (str "<div style=\""menu-bar-style"\">"
            (generate-button)
            "</div>")))

(defn- docs
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [request]
  (str (menu-bar request)
       "<div style=\"padding:20px 50px\">"
       "</br>Connected libraries:"
       "</br>ajax-api"
       "</br>bybit-api"
       "</br>cljc-fruits"
       "</br>docs-api"
       "</br>dom-api"
       "</br>io-api"
       "</br>local-db-api"
       "</br>logger-api"
       "</br>mongo-db-api"
       "</br>re-frame-api"
       "</br>time-api"
       "</br>window-api"
       (status request)
       "</div>"))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [query-params] :as request}]
  (if-let [generate-docs? (get query-params "generate-docs")]
          (generate-docs request)
          (docs          request)))
