
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.07
; Description:
; Version: v0.3.8
; Compatibility: x4.5.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.mongo-browser
    (:require [mid-fruits.pretty  :as pretty]
              [mid-fruits.string  :as string]
              [mongo-db.api       :as mongo-db]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [x.server-db.api    :as db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [collection-name]}]
  (if (string/nonempty? collection-name)
      (str "<div><a href=\"?\">..</a></div>")
      (str "<div>..</div>")))

(defn- document
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [collection-name]} document]
  (let [document-id (db/document->document-id document)]
       (str "<br/><div><a href=\"?collection-name="collection-name"&remove-document="document-id"\">Remove document!</a></div>"
            "<pre>" (pretty/mixed->string document) "</pre>")))

(defn- collection
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [collection-name] :as browser-props}]
  (let [all-documents (mongo-db/get-all-documents collection-name)]
       (str "<br/><div><a href=\"?empty-collection=" collection-name "\">Empty collection!</a></div>"
            (reduce #(str %1 (document browser-props %2))
                    "" all-documents))))

(defn- db
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [collection-names (mongo-db/get-collection-names)]
       (reduce #(str %1 (str "<div><a href=\"?collection-name="%2"\">"%2"</a></div>"))
               "" collection-names)))

(defn- mongo-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [collection-name empty-collection remove-document] :as browser-props}]
  (str (up-button browser-props)
       (cond (string/nonempty? empty-collection)
             (do (mongo-db/remove-all-documents! empty-collection)
                 (db browser-props))
             (string/nonempty? remove-document)
             (do (mongo-db/remove-document! collection-name remove-document)
                 (collection browser-props))
             (string/nonempty? collection-name)
             (collection browser-props)
             :else (db browser-props))))

(defn- print-mongo-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [browser-props]
  (mongo-db/get-collection-names)
  (str "<html>"
       "<body>"
       "<pre style=\"white-space: normal\">"
       (mongo-browser browser-props)
       "</pre>"
       "</body>"
       "</html>"))

(defn- download-mongo-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [query-params]}]
  (let [collection-name  (get query-params "collection-name")
        empty-collection (get query-params "empty-collection")
        remove-document  (get query-params "remove-document")]
       (http/html-wrap {:body (print-mongo-browser {:collection-name  collection-name
                                                    :empty-collection empty-collection
                                                    :remove-document  remove-document})})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-boot {:dispatch-n [[:router/add-route! :developer/mongo-browser-route
                                                     {:route-template "/developer/mongo-browser"
                                                      :get (fn [request] (download-mongo-browser request))}]]}})
                                                     ;:restricted? true]]}})
