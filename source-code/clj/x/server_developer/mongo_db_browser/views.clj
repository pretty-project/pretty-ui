
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.mongo-db-browser.views
    (:require [mid-fruits.pretty  :as pretty]
              [mid-fruits.string  :as string]
              [mongo-db.api       :as mongo-db]
              [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [x.server-db.api    :as db]
              [x.server-developer.mongo-db-browser.styles :as mongo-db-browser.styles]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [collection-name]}]
  (if (string/nonempty? collection-name)
      (let [menu-button-style (mongo-db-browser.styles/menu-button-style)]
           (str "<a style=\""menu-button-style"\" href=\"?\">Back to collections</a>"))))

(defn- empty-collection-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [collection-name]}]
  (if (string/nonempty? collection-name)
      (let [menu-button-style (mongo-db-browser.styles/menu-button-style {:warning? true})]
           (str "<a style=\""menu-button-style"\" href=\"?empty-collection="collection-name"\">Empty collection</a>"))))

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [browser-props]
  (let [menu-bar-style (mongo-db-browser.styles/menu-bar-style)]
       (str "<div style=\""menu-bar-style"\">"
            (up-button               browser-props)
            (empty-collection-button browser-props)
            "</div>")))

(defn- document
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [collection-name]} document-dex document]
  (let [document-id    (db/document->document-id document)
        document-style (mongo-db-browser.styles/document-style {:document-dex document-dex})
        button-style   (mongo-db-browser.styles/remove-button-style)]
       (str "<div style=\""document-style"; position: relative\">"
            "<div style=\"position: absolute; display: flex; justify-content: right; top: 12px; right: 12px\">"
            "<a style=\""button-style"\" href=\"?collection-name="collection-name"&remove-document="document-id"\">Remove document</a>"
            "</div>"
            "<div style=\"position: absolute; display: flex; top: 48px; right: 24px; color: #aaa\">"collection-name" | "document-dex"</div>"
            "<pre>" (pretty/mixed->string document) "</pre>"
            "</div>")))

(defn- collection
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [collection-name] :as browser-props}]
  (let [all-documents (mongo-db/get-all-documents collection-name)]
       (reduce-kv #(str %1 (document browser-props %2 %3))
               "" all-documents)))

(defn- db
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [collection-names        (mongo-db/get-collection-names)
        collection-button-style (mongo-db-browser.styles/collection-button-style)]
       (reduce #(str %1 (str "<div><a style=\""collection-button-style"\" href=\"?collection-name="%2"\">"%2"</a></div>"))
               "" (sort collection-names))))

(defn- refresh-page
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [collection-name remove-document]}]
  (str "<script type=\"text/javascript\">window.location.href="
       (if remove-document (str "'?collection-name="collection-name"'")
                           (str "'?'"))
       "</script>"))

(defn- mongo-db-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [collection-name empty-collection remove-document] :as browser-props}]
  (cond ; Emptying the collection ...
        (string/nonempty? empty-collection)
        (do (mongo-db/remove-all-documents! empty-collection)
            (refresh-page browser-props))
        ; Removing the document ...
        (string/nonempty? remove-document)
        (do (mongo-db/remove-document! collection-name remove-document)
            (refresh-page browser-props))
        ; Collection view
        (string/nonempty? collection-name)
        (str (menu-bar   browser-props)
             (collection browser-props))
        ; Database view
        :database-view
        (str (menu-bar browser-props)
             (db       browser-props))))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [query-params]}]
  (let [collection-name  (get query-params "collection-name")
        empty-collection (get query-params "empty-collection")
        remove-document  (get query-params "remove-document")]
       (mongo-db-browser {:collection-name  collection-name
                          :empty-collection empty-collection
                          :remove-document  remove-document})))
