
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.head.helpers
    (:require [mid-fruits.candy         :refer [param return]]
              [mid-fruits.string        :as string]
              [mid-fruits.time          :as time]
              [mid-fruits.vector        :as vector]
              [server-fruits.http       :as http]
              [x.app-details            :as details]
              [x.server-core.api        :as a :refer [cache-control-uri]]
              [x.server-ui.core.helpers :refer [include-css include-favicon include-font]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn meta-keywords->formatted-meta-keywords
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string or strings in vector) meta-keywords
  ;
  ; @example
  ;  (meta-keywords->formatted-meta-keywords ["My" "keywords"])
  ;  =>
  ;  "My, keywords"
  ;
  ; @example
  ;  (meta-keywords->formatted-meta-keywords "My, keywords")
  ;  =>
  ;  "My, keywords"
  ;
  ; @return (string)
  [meta-keywords]
  (if (string?     meta-keywords)
      (return      meta-keywords)
      (string/join meta-keywords ", ")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn head<-crawler-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:crawler-rules (string)
  ;   :meta-description (string)
  ;   :meta-keywords (string or strings in vector)
  ;   :meta-name (string)
  ;   :meta-title (string)}
  ;
  ; @return (hiccup)
  [head _ {:keys [crawler-rules meta-description meta-keywords meta-name meta-title]}]
  (let [meta-keywords (meta-keywords->formatted-meta-keywords meta-keywords)]
       (vector/concat-items head [[:meta {:content crawler-rules    :name "robots"}]
                                  [:meta {:content meta-description :name "description"}]
                                  [:meta {:content meta-title       :name "title"}]])))

(defn head<-share-preview-properties
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:meta-description (string)
  ;   :meta-name (string)
  ;   :share-preview-uri (string)}
  ;
  ; @return (hiccup)
  [head _ {:keys [meta-description meta-name share-preview-uri]}]
  (vector/concat-items head [[:meta {:content meta-name         :itemprop "name"}]
                             [:meta {:content meta-description  :itemprop "description"}]
                             [:meta {:content share-preview-uri :itemprop "image"}]]))

(defn head<-browser-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:app-title (string)
  ;   :selected-language (keyword)
  ;   :theme-color (string)}
  ;
  ; @return (hiccup)
  [head _ {:keys [app-title selected-language theme-color]}]
  (vector/concat-items head [[:title app-title]
                             [:meta {:charset "utf-8"}]
                             ; maximum-scale=1
                             ; A mobileszköz böngészők ne nagyítsák a tartalmat input elemek kitöltése közben.
                             ; https://stackoverflow.com/questions/2989263/disable-auto-zoom-in-input-text-tag-safari-on-iphone
                             [:meta {:content "width=320, initial-scale=1 maximum-scale=1" :name "viewport"}]
                             [:meta {:content theme-color                                  :name "theme-color"}]
                             [:meta {:content selected-language                            :http-equiv "content-language"}]]))

(defn head<-legal-information
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:author (string)}
  ;
  ; @return (hiccup)
  [head _ {:keys [author]}]
  (let [current-year          (time/get-year)
        copyright-information (details/copyright-information current-year)]
       (vector/concat-items head [[:meta {:content author                :name "author"}]
                                  [:meta {:content details/app-version   :name "version"}]
                                  [:meta {:content copyright-information :name "copyright"}]])))

(defn head<-og-properties
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; The Open Graph protocol
  ; https://ogp.me/
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:app-title (string)
  ;   :meta-description (string)
  ;   :share-preview-uri (string)}
  ;
  ; @return (hiccup)
  [head request {:keys [app-title meta-description share-preview-uri]}]
  (let [og-url (http/request->uri request)]
       (vector/concat-items head [[:meta {:content "website"         :property "og:type"}]
                                  [:meta {:content meta-description  :property "og:description"}]
                                  [:meta {:content app-title         :property "og:title"}]
                                  [:meta {:content share-preview-uri :property "og:image"}]
                                  [:meta {:content og-url            :property "og:url"}]])))

(defn head<-css-includes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:app-build (string)(opt)
  ;   :core-js (string)
  ;   :css-paths (maps in vector)
  ;    [{:core-js (string)(opt)
  ;      :uri (string)}]
  ;
  ; @return (hiccup)
  [head _ {:keys [app-build core-js css-paths] :as head-props}]
  (letfn [(include-css? [css-props] (or (-> css-props :core-js nil?)
                                        (-> css-props :core-js (= core-js))))
          (f [head {:keys [uri] :as css-props}]
             (let [cache-control-uri (cache-control-uri uri app-build)
                   css-props         (assoc css-props :uri cache-control-uri)]
                  (if (include-css? css-props)
                      (conj   head (include-css css-props))
                      (return head))))]
         (reduce f head css-paths)))

(defn head<-favicon-includes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (hiccup) head
  ; @param (map) request
  ; @param (map) head-props
  ;  {:app-build (string)(opt)
  ;   :core-js (string)
  ;   :favicon-paths (maps in vector)
  ;    [{:core-js (string)(opt)
  ;      :size (string)
  ;      :uri (string)}]
  ;
  ; @return (hiccup)
  [head _ {:keys [app-build core-js favicon-paths]}]
  (letfn [(include-favicon? [favicon-props] (or (-> favicon-props :core-js nil?)
                                                (-> favicon-props :core-js (= core-js))))
          (f [head {:keys [uri] :as favicon-props}]
             (let [cache-control-uri (cache-control-uri uri app-build)
                   favicon-props     (assoc favicon-props :uri cache-control-uri)]
                  (if (include-favicon? favicon-props)
                      (conj   head (include-favicon favicon-props))
                      (return head))))]
         (reduce f head favicon-paths)))
